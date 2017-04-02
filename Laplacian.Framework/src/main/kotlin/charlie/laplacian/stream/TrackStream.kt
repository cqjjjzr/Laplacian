package charlie.laplacian.stream

interface TrackStream {
    fun position(): Int
    fun read(buffer: ByteArray, offset: Int, length: Int): Int
    fun close()
    fun forceSeek(positionBytes: Int)
}

interface SizeKnownTrackStream: TrackStream {
    fun size(): Int
}

interface SeekableTrackStream: TrackStream {
    fun seek(positionBytes: Int)
}