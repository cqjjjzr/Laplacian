package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.decoder.DecoderSession
import charlie.laplacian.decoder.essential.ffmpeg.internal.FFmpegDecodeBridge
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import java.util.concurrent.locks.ReentrantLock

class FFmpegDecoderSession(outputSettings: OutputSettings, stream: TrackStream): DecoderSession {
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
}

class FFmpegDecoder : Decoder {
    companion object {
        fun init() {
            FFmpegDecodeBridge.init()
        }
    }

    override fun getSession(outputSettings: OutputSettings, stream: TrackStream): DecoderSession {
        return FFmpegDecoderSession(outputSettings, stream)
    }

    override fun getMetadata(): DecoderMetadata = FFmpegDecoderMetadata

    override fun hashCode(): Int = this.javaClass.name.hashCode()
    override fun equals(other: Any?): Boolean = other is FFmpegDecoder
}

object FFmpegDecoderMetadata: DecoderMetadata {
    override fun getName(): String = "Essential.FFmpegDecoder"

    override fun getVersion(): String = "rv1"

    override fun getVersionID(): Int = 1
}