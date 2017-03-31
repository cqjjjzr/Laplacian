package charlie.laplacian.source

import charlie.laplacian.stream.TrackStream

interface TrackSource {
    fun getName(): String
    fun getDescription(): String
    fun streamFrom(source: TrackSourceInfo): TrackStream
}

interface LocalTrackSource: TrackSource
interface RemoteTrackSource: TrackSource