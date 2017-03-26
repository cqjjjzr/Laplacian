package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderException
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.DecoderMetadata
import charlie.laplacian.mixer.Mixer
import charlie.laplacian.stream.TrackStream
import java.util.*

object DecoderRegistry {
    private val decoderFactories: MutableList<DecoderFactory> = LinkedList()

    fun registerDecoderFactory(factory: DecoderFactory) {
        if (!decoderFactories.contains(factory))
            decoderFactories += factory
    }

    fun unregisterDecoderFactory(factory: DecoderFactory) {
        decoderFactories -= factory
    }

    fun getMetadatas(): Array<DecoderMetadata> {
        return Array(decoderFactories.size, { decoderFactories[it].getMetadata() })
    }

    fun moveUp(index: Int) {
        if (index == 0 || decoderFactories.size <= 1) throw IllegalArgumentException()
        decoderFactories[index - 1].apply {
            decoderFactories[index - 1] = decoderFactories[index]
            decoderFactories[index] = this
        }
    }

    fun moveDown(index: Int) {
        if (index == decoderFactories.size - 1 || decoderFactories.size <= 1) throw IllegalArgumentException()
        decoderFactories[index].apply {
            decoderFactories[index] = decoderFactories[index + 1]
            decoderFactories[index + 1] = this
        }
    }

    fun tryDecode(mixer: Mixer, sampleRateHz: Float, bitDepth: Int, numChannel: Int, stream: TrackStream): Decoder {
        decoderFactories.forEach {
            try {
                return it.getDecoder(mixer, sampleRateHz, bitDepth, numChannel, stream)
            } catch (ex: Exception) {

            }
        }
        throw DecoderException()
    }
}