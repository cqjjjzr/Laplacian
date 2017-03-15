package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.SeekableTrackStream
import charlie.laplacian.decoder.TrackStream
import io.humble.video.customio.IURLProtocolHandler
import io.humble.video.customio.IURLProtocolHandler.URL_RDONLY_MODE
import io.humble.video.customio.IURLProtocolHandlerFactory

class HumbleVideoProtocolHandler: IURLProtocolHandler {
    private val SUCCESS: Int = 0
    private val FAIL: Int = -1

    private var stream: TrackStream? = null

    override fun open(url: String, flags: Int): Int {
        if (flags != URL_RDONLY_MODE) return FAIL
        url.split("//").apply {
            if (size < 2) throw IllegalArgumentException("bad url format")
            stream = FFmpegDecoder.streams[this[1]]
            return SUCCESS
        }
        return FAIL
    }

    override fun write(buf: ByteArray, size: Int): Int {
        throw UnsupportedOperationException()
    }

    override fun isStreamed(url: String, flags: Int): Boolean {
        return false
    }

    override fun seek(offset: Long, whence: Int): Long {
        if (stream is SeekableTrackStream) {
            (stream as SeekableTrackStream).seek((whence + offset).toInt())
            return stream!!.position().toLong()
        }
        return -1L
    }

    override fun close(): Int {
        stream!!.close()
        return SUCCESS
    }

    override fun read(buf: ByteArray, size: Int): Int {
        return stream!!.read(buf, 0, size)
    }
}

class HumbleVideoProtocolHandlerFactory: IURLProtocolHandlerFactory {
    override fun getHandler(protocol: String?, url: String?, flags: Int): IURLProtocolHandler {
        if (flags != URL_RDONLY_MODE)
            throw UnsupportedOperationException()
        if (protocol == "laplacian")
            return HumbleVideoProtocolHandler()
        throw IllegalArgumentException()
    }
}