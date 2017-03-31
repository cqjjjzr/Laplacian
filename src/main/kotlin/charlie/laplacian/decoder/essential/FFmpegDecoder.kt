package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.output.OutputDevice
import charlie.laplacian.output.OutputLine
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.plugin.Plugin
import charlie.laplacian.stream.TrackStream
import charlie.laplacian.stream.essential.FileTrackStream
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class FFmpegDecoder: Decoder {
    companion object {
        @JvmStatic
        private external fun globalInit()
        fun init() {
            System.loadLibrary("libFFmpegDecoder")
            globalInit()
        }
    }

    private var audioChannel: OutputLine

    @Volatile
    private var paused: Boolean = true
    @Volatile
    private var closed: Boolean = false
    private var pointerAVCodecContext: Long = 0
    private var pointerAVFormatContext: Long = 0

    @Volatile
    private var position: Long = 0
    private var duration: Long = 0

    @Volatile
    private var audioStreamIndex: Int = -1
    private var pauseLock: Lock = ReentrantLock(true)
    private val pauseCondition: Condition = pauseLock.newCondition()

    private val mixer: OutputDevice
    private val numChannel: Int
    private val sampleRateHz: Float
    private val bitDepth: Int

    private val thread =
            thread (
                start = false,
                name = "FFmpegDecoder-PlayThread-" + hashCode(),
                isDaemon = true) {
                    try {
                        playThread()
                        if (!closed)
                            close()
                    } catch(ex: Exception) {
                        ex.printStackTrace()
                    }
                }

    override fun play() {
        paused = false
        audioChannel.open()

        pauseLock.lock()
        pauseCondition.signalAll()
        pauseLock.unlock()
    }

    override fun pause() {
        paused = true
        audioChannel.pause()
    }

    override external fun seek(positionMillis: Long)

    override fun positionMillis(): Long = position

    override fun durationMillis(): Long = duration

    override fun close() {
        closed = true
        while (thread.isAlive)
            Thread.sleep(1)
        closeInternal()
        mixer.closeLine(audioChannel)
    }

    external fun closeInternal()

    private external fun playThread()

    private external fun startupNativeLibs(sampleRateHz: Float, bitDepth: Int, numChannel: Int)

    private external fun initWithStream(stream: TrackStream)

    private external fun initWithURL(url: String)

    private fun internalMix(pcmData: ByteArray, offset: Int, length: Int) {
        audioChannel.mix(pcmData, offset, length)
    }

    private constructor(mixer: OutputDevice, outputSettings: OutputSettings) {
        this.mixer = mixer
        this.sampleRateHz = outputSettings.sampleRateHz
        this.bitDepth = outputSettings.bitDepth
        this.numChannel = outputSettings.numChannels
        if (bitDepth != 32 && bitDepth != 16 && bitDepth != 64)
            throw IllegalArgumentException("bit depth must be 32, 64 or 16")
        audioChannel = mixer.openLine()
    }

    constructor(mixer: OutputDevice, outputSettings: OutputSettings, stream: TrackStream)
            : this(mixer, outputSettings) {
        initWithStream(stream)
        init(outputSettings)
    }

    constructor(mixer: OutputDevice, outputSettings: OutputSettings, url: String)
            : this(mixer, outputSettings) {
        initWithURL(url)
        init(outputSettings)
    }

    private fun init(outputSettings: OutputSettings) {
        outputSettings.apply {
            startupNativeLibs(sampleRateHz, bitDepth, numChannel)
        }
        thread.start()
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(mixer: OutputDevice, outputSettings: OutputSettings, stream: TrackStream): Decoder {
        if (stream is FileTrackStream)
            return FFmpegDecoder(mixer, outputSettings, stream.getPath().toString())
        // TODO Add more simple URLStream here
        return FFmpegDecoder(mixer, outputSettings, stream)
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata

    override fun hashCode(): Int = this.javaClass.name.hashCode()
    override fun equals(other: Any?): Boolean = other is FFmpegDecoderFactory
}

object FFmpegDecoderMetadata: DecoderMetadata {
    override fun getName(): String = "Essential.FFmpegDecoder"

    override fun getVersion(): String = "rv1"

    override fun getVersionID(): Int = 1

    override fun getPlugin(): Plugin {
        TODO("not implemented")
    }
}