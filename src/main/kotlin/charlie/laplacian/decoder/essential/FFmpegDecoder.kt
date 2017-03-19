package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.TrackStream

class FFmpegDecoder(stream: TrackStream): Decoder {
    companion object {
        @JvmStatic
        private external fun globalInit()
    }

    private val URL_PREFIX = "laplacian://"
    private val serial: String = System.nanoTime().toString()
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

    override external fun play()

    override external fun stop()

    override external fun pause()

    override external fun seek(positionMillis: Long)

    override fun positionMillis(): Long = position

    override external fun durationMillis(): Long

    override fun volumeTo(percent: Int) {
        volume = (percent * 1.28).toInt()
    }

    override fun volume(): Int = (volume / 1.28).toInt()

    override external fun close()

    private external fun playThread()

    private external fun initNativeLib(stream: TrackStream)

    init {
        initNativeLib(stream) // CustomIO free for url input
        //TODO 用file, url这样的简单IO不用CustomIO
    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(stream: TrackStream): Decoder {
        return FFmpegDecoder(stream)
    }
}