package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.mixer.AudioChannel
import charlie.laplacian.mixer.Mixer
import charlie.laplacian.stream.TrackStream
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
        startupThread()
    }

    private fun startupThread() {
        thread (start = true,
                name = "FFmpegDecoder-PlayThread-" + hashCode(),
                isDaemon = true) {
            try {
                playThread()
                close()
            } catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream): Decoder {
        //if (stream is FileTrackStream)
            //return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream.getPath().toString())
        // TODO Add more simple URLStream here
        return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream)
    }
}