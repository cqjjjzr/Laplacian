package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.TrackStream
import charlie.laplacian.decoder.VolumeController
import kotlin.concurrent.thread

class FFmpegDecoder(defaultVolume: Int): Decoder {
    companion object {
        @JvmStatic
        private external fun globalInit()
        fun init() {
            System.loadLibrary("libFFmpegDecoder")
            globalInit()
        }
    }

    private val volumeController = SDLVolumeController(defaultVolume)
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

    override external fun close()

    override fun getVolumeController(): VolumeController = volumeController

    private fun getVolumeInternal(): Int = volumeController.currentInInt()

    private external fun playInternal()

    private external fun pauseInternal()

    private external fun playThread()

    private external fun startupNativeLibs()

    private external fun initWithStream(stream: TrackStream)

    private external fun initWithURL(url: String)

    constructor(defaultVolume: Int, stream: TrackStream) : this(defaultVolume) {
        initWithStream(stream)
        init()
    }

    constructor(defaultVolume: Int, url: String) : this(defaultVolume) {
        initWithURL(url)
        init()
    }

    private fun init() {
        startupNativeLibs()
        startupThread()
    }

    private fun startupThread() {
        thread (start = true,
                name = "FFmpegDecoder-PlayThread-" + hashCode(),
                isDaemon = true) {
            playThread()
        }
    }

    private inner class SDLVolumeController(private var volume: Int): VolumeController {
        override fun max(): Float = 128.0f

        override fun min(): Float = 0.0f

        override fun current(): Float = volume.toFloat()

        fun currentInInt(): Int = volume

        override fun set(value: Float) {
            volume = value.toInt()
        }

    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(previousVolume: Float, stream: TrackStream): Decoder {
        if (stream is FileTrackStream) return FFmpegDecoder(previousVolume.toInt(), stream.getPath().toString())
        // TODO Add more simple URLStream here
        return FFmpegDecoder(previousVolume.toInt(), stream)
    }
}