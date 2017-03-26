package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.mixer.AudioChannel
import charlie.laplacian.mixer.Mixer
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

    private var audioChannel: AudioChannel

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

    private val mixer: Mixer
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
        mixer.closeChannel(audioChannel)
    }

    external fun closeInternal()

    private external fun playThread()

    private external fun startupNativeLibs(sampleRateHz: Float, bitDepth: Int, numChannel: Int)

    private external fun initWithStream(stream: TrackStream)

    private external fun initWithURL(url: String)

    private fun internalMix(pcmData: ByteArray, offset: Int, length: Int) {
        audioChannel.mix(pcmData, offset, length)
    }

    private constructor(mixer: Mixer,
                        sampleRateHz: Float,
                        bitDepth: Int,
                        numChannel: Int) {
        this.mixer = mixer
        this.sampleRateHz = sampleRateHz
        this.bitDepth = bitDepth
        this.numChannel = numChannel
        if (bitDepth != 32 && bitDepth != 16 && bitDepth != 64)
            throw IllegalArgumentException("bit depth must be 32, 64 or 16")
        audioChannel = mixer.openChannel()
    }

    constructor(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream)
            : this(mixer, sampleRateHz, bitDepth, numChannel) {
        initWithStream(stream)
        init(sampleRateHz, bitDepth, numChannel)
    }

    constructor(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, url: String)
            : this(mixer, sampleRateHz, bitDepth, numChannel) {
        initWithURL(url)
        init(sampleRateHz, bitDepth, numChannel)
    }

    private fun init(sampleRateHz: Float, bitDepth: Int, numChannel: Int) {
        startupNativeLibs(sampleRateHz, bitDepth, numChannel)
        thread.start()
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream): Decoder {
        if (stream is FileTrackStream)
            return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream.getPath().toString())
        // TODO Add more simple URLStream here
        return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream)
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