package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.decoder.essential.ffmpeg.internal.FFmpegDecodeBridge
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import java.util.concurrent.locks.ReentrantLock

class FFmpegDecoder(outputSettings: OutputSettings, stream: TrackStream): Decoder {
    companion object {
        fun init() {
            FFmpegDecodeBridge.init()
        }
    }

    private val bridge = FFmpegDecodeBridge(stream, outputSettings)
    private val lock = ReentrantLock()

    override fun seek(positionMillis: Long) {
        lock.lock()
        bridge.seek(positionMillis)
        lock.unlock()
    }

    override fun positionMillis(): Long = bridge.positionMillis

    override fun durationMillis(): Long = bridge.durationMillis

    override fun close() {
        lock.lock()
        bridge.close()
        lock.unlock()
    }

    override fun read(buf: ByteArray?): ByteArray? {
        lock.lock()
        val rbuf = bridge.tryRead(buf)
        lock.unlock()
        return rbuf
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(outputSettings: OutputSettings, stream: TrackStream): Decoder {
        return FFmpegDecoder(outputSettings, stream)
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