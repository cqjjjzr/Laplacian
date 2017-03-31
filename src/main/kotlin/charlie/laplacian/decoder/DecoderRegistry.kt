package charlie.laplacian.decoder

import charlie.laplacian.output.OutputDevice
import charlie.laplacian.output.OutputSettings
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

    fun getMetadatas(): Array<DecoderMetadata> = Array(decoderFactories.size, { decoderFactories[it].getMetadata() })

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

    fun tryDecode(mixer: OutputDevice, outputSettings: OutputSettings, stream: TrackStream): Decoder {
        var e: Throwable? = null
        decoderFactories.forEach {
            try {
                return it.getDecoder(mixer, outputSettings, stream)
            } catch (ex: Exception) {
                e = ex
            }
        }
        if (e == null)
            throw DecoderException()
        else
            throw DecoderException(e)
    }
}