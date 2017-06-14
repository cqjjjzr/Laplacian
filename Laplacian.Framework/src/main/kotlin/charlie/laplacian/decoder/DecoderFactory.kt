package charlie.laplacian.decoder

import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream

interface DecoderFactory {
    fun getDecoder(outputSettings: OutputSettings, stream: TrackStream): Decoder

    fun getMetadata(): DecoderMetadata
}