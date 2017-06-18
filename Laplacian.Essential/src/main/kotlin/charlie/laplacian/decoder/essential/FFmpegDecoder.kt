package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.decoder.essential.ffmpeg.internal.FFmpegDecodeBridge
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class FFmpegDecoder: Decoder {
    companion object {
        private val logger: Logger = LogManager.getLogger("decoder")
        @JvmStatic
        private fun writeLog(message: String, levelID: Int) {
            logger.log(
                    when(levelID) { // SEE FFmpeg libavutil/log.h
                        -8 -> Level.TRACE
                        0, 8 -> Level.FATAL
                        16 -> Level.ERROR
                        24 -> Level.WARN
                        32 -> Level.INFO
                        40, 48 -> Level.DEBUG
                        56 -> Level.TRACE
                        else -> Level.TRACE
                    }, message)
        }
        fun init() {
            FFmpegDecodeBridge.init()
        }
    }

    @Volatile
    private var closed: Boolean = false
    private var pointerAVCodecContext: Long = 0
    private var pointerAVFormatContext: Long = 0

    @Volatile
    private var position: Long = 0
    private var duration: Long = 0

    @Volatile
    private var audioStreamIndex: Int = -1

    private val numChannel: Int
    private val sampleRateHz: Float
    private val bitDepth: Int

    override external fun seek(positionMillis: Long)

    override fun positionMillis(): Long = position

    override fun durationMillis(): Long = duration

    override fun close() {
        closed = true
        closeInternal()
    }

    private external fun closeInternal()

    override external fun read(): ByteArray
    override external fun hasNext(): Boolean

    private external fun startupNativeLibs(sampleRateHz: Float, bitDepth: Int, numChannel: Int)

    private external fun initWithStream(stream: TrackStream)

    private external fun initWithURL(url: String)

    private constructor(outputSettings: OutputSettings) {
        this.sampleRateHz = outputSettings.sampleRateHz
        this.bitDepth = outputSettings.bitDepth
        this.numChannel = outputSettings.numChannels
        if (bitDepth != 32 && bitDepth != 16 && bitDepth != 64)
            throw IllegalArgumentException("bit depth must be 32, 64 or 16")
    }

    constructor(outputSettings: OutputSettings, stream: TrackStream)
            : this(outputSettings) {
        initWithStream(stream)
        init(outputSettings)
    }

    private fun init(outputSettings: OutputSettings) {
        outputSettings.apply {
            startupNativeLibs(sampleRateHz, bitDepth, numChannel)
        }
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(outputSettings: OutputSettings, stream: TrackStream): Decoder {
        return FFmpegDecoder( outputSettings, stream)
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata

    override fun hashCode(): Int = this.javaClass.name.hashCode()
    override fun equals(other: Any?): Boolean = other is FFmpegDecoderFactory
}

object FFmpegDecoderMetadata: DecoderMetadata {
    override fun getName(): String = "Essential.FFmpegDecoder"

    override fun getVersion(): String = "rv1"

    override fun getVersionID(): Int = 1
}