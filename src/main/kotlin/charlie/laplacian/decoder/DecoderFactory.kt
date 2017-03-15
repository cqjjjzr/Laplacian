package charlie.laplacian.decoder

interface DecoderFactory {
    fun getDecoder(stream: TrackStream): Decoder
}