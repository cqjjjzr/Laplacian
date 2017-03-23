package charlie.laplacian.stream

interface SizeKnownTrackStream: TrackStream {
    fun size(): Int
}