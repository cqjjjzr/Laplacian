package charlie.laplacian.decoder.essential

/*class HumbleVideoProtocolHandler: IURLProtocolHandler {
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

    override fun write(buf: ByteArray, size: Int): Int = FAIL

    override fun isStreamed(url: String, flags: Int): Boolean = false

    override fun seek(offset: Long, whence: Int): Long {
        if (stream is SeekableTrackStream) {
            (stream as SeekableTrackStream).apply {
                when (whence) {
                    SEEK_SET -> seek(offset.toInt())
                    SEEK_CUR -> seek(position() + offset.toInt())
                    SEEK_END -> {
                        if (stream is SizeKnownTrackStream)
                            seek((stream as SizeKnownTrackStream).size() - offset.toInt())
                        else return -1L
                    }
                    SEEK_SIZE -> {
                        if (stream is SizeKnownTrackStream)
                            return (stream as SizeKnownTrackStream).size().toLong()
                        else return -1L
                    }
                    else -> {
                        return 1L
                    }
                }
            }
            return stream!!.position().toLong()
        }
        return -1L
    }

    override fun close(): Int {
        stream!!.close()
        return SUCCESS
    }

    override fun read(buf: ByteArray, size: Int): Int {
        stream!!.read(buf, 0, size).apply {
            when (this) {
                -1 -> return 0 // EOF
                else -> return this
            }
        }
        return -1
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
}*/