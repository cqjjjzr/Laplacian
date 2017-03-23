package charlie.laplacian.decoder

import charlie.laplacian.mixer.Mixer
import charlie.laplacian.stream.TrackStream

interface DecoderFactory {
    fun getDecoder(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream): Decoder
}