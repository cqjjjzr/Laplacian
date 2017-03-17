package charlie.laplacian.decoder

interface SizeKnownTrackStream: TrackStream {
    fun size(): Int
}