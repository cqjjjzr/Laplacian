package charlie.laplacian.decoder

import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import java.util.*
import kotlin.NoSuchElementException

object DecoderRegistry {
    private val decoders: MutableList<Decoder> = LinkedList()

    fun registerDecoderFactory(factory: Decoder) {
        if (!decoders.contains(factory))
            decoders += factory
    }

    fun unregisterDecoderFactory(factory: Decoder) {
        decoders -= factory
    }

    fun getMetadatas(): Array<DecoderMetadata> = Array(decoders.size, { decoders[it].getMetadata() })

    fun moveUp(index: Int) {
        if (index == 0 || decoders.size <= 1) throw IllegalArgumentException()
        decoders[index - 1].apply {
            decoders[index - 1] = decoders[index]
            decoders[index] = this
        }
    }

    fun moveDown(index: Int) {
        if (index == decoders.size - 1 || decoders.size <= 1) throw IllegalArgumentException()
        decoders[index].apply {
            decoders[index] = decoders[index + 1]
            decoders[index + 1] = this
        }
    }

    fun tryDecode(outputSettings: OutputSettings, stream: TrackStream): DecoderSession {
        var e: Throwable? = null
        decoders.forEach {
            try {
                return it.getSession(outputSettings, stream)
            } catch (ex: Exception) {
                e = ex
            }
        }
        if (e == null)
            throw DecoderException(NoSuchElementException())
        else
            throw DecoderException(e)
    }
}