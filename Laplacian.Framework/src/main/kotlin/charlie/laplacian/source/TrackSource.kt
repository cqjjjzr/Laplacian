package charlie.laplacian.source

import charlie.laplacian.stream.TrackStream

interface TrackSource {
    fun getMetadata(): TrackSourceMetadata
    fun streamFrom(source: TrackSourceInfo): TrackStream
}

interface LocalTrackSource: TrackSource
interface RemoteTrackSource: TrackSource

interface TrackSourceMetadata {
    fun getName(): String
    fun getDescription(): String
    fun getSupportedTrackInfos(): Array<Class<out TrackSourceInfo>>

    fun getVersion(): String
    fun getVersionID(): Int
}