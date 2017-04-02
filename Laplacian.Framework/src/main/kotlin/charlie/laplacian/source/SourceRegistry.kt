package charlie.laplacian.source

import charlie.laplacian.stream.TrackStream
import java.util.*

object SourceRegistry {
    private val sources: MutableList<TrackSource> = LinkedList()

    fun registerSource(source: TrackSource) {
        sources += source
    }

    fun unregisterSource(source: TrackSource) {
        sources -= source
    }

    fun getMetadatas(): Array<TrackSourceMetadata> = Array(sources.size) { sources[it].getMetadata() }

    fun getStream(info: TrackSourceInfo): TrackStream =
            sources.filter { it.getMetadata().getSupportedTrackInfos().contains(info.javaClass) }
                    .first().streamFrom(info)
}