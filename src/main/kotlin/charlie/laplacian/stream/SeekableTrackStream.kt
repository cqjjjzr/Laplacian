package charlie.laplacian.stream

interface SeekableTrackStream: TrackStream {
    fun seek(positionBytes: Int)
}