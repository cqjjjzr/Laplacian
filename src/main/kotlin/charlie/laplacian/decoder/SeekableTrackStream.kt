package charlie.laplacian.decoder

interface SeekableTrackStream: TrackStream {
    fun seek(positionBytes: Int)
}