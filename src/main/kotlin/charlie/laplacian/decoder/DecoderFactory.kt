package charlie.laplacian.decoder

interface DecoderFactory {
    fun getDecoder(previousVolume: Float, stream: TrackStream): Decoder
}