package charlie.laplacian.source

import charlie.laplacian.decoder.TrackStream

interface TrackSource {
    fun getName(): String
    fun getDescription(): String
    fun streamFrom(source: TrackSourceInfo): TrackStream
}