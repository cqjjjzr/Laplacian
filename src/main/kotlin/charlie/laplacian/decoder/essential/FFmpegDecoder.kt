package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.mixer.AudioChannel
import charlie.laplacian.mixer.Mixer
import charlie.laplacian.stream.TrackStream
import charlie.laplacian.stream.essential.FileTrackStream
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

    private val mixer: Mixer
    private var audioChannel: AudioChannel

    @Volatile
    private var paused: Boolean = true
    @Volatile
    private var pointerAVCodecContext: Long = 0
    @Volatile
    private var pointerAVFormatContext: Long = 0
    @Volatile
    private var pointerPacketQueue: Long = 0
    @Volatile
    private var volume: Int = 0
    private var position: Long = 0
    @Volatile
    private var audioStreamIndex: Int = -1

    override fun play() {
        paused = false
        playInternal()
        synchronized (this) {
            (this as Object).notifyAll()
        }
    }

    override fun pause() {
        paused = true
        pauseInternal()
    }

    override external fun seek(positionMillis: Long)

    override fun positionMillis(): Long = position

    override external fun durationMillis(): Long

    override fun close() {
        closeInternal()
        mixer.closeChannel(audioChannel)
    }

    external fun closeInternal()

    private external fun playInternal()

    private external fun pauseInternal()

    private external fun playThread()

    private external fun startupNativeLibs(sampleRateHz: Float, bitDepth: Int, numChannel: Int)

    private external fun initWithStream(stream: TrackStream)

    private external fun initWithURL(url: String)

    private fun internalMix(pcmData: ByteArray, offset: Int, length: Int) {
        audioChannel.mix(pcmData, offset, length)
    }

    constructor(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream) {
        this.mixer = mixer
        audioChannel = mixer.openChannel()
        initWithStream(stream)
        init(sampleRateHz, bitDepth, numChannel)
    }

    constructor(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, url: String) {
        this.mixer = mixer
        audioChannel = mixer.openChannel()
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
            playThread()
        }
    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream): Decoder {
        if (stream is FileTrackStream)
            return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream.getPath().toString())
        // TODO Add more simple URLStream here
        return FFmpegDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream)
    }
}