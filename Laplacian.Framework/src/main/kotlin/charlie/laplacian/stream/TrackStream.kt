package charlie.laplacian.stream

import java.io.IOException

interface TrackStream {
    @Throws(IOException::class)
    fun position(): Int
    @Throws(IOException::class)
    fun read(buffer: ByteArray, offset: Int, length: Int): Int
    @Throws(IOException::class)
    fun close()
    @Throws(IOException::class)
    fun forceSeek(positionBytes: Int)
}

interface SizeKnownTrackStream: TrackStream {
    fun size(): Int
}

interface SeekableTrackStream: TrackStream {
    fun seek(positionBytes: Int)
}