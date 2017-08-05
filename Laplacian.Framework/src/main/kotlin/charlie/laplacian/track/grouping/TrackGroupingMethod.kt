package charlie.laplacian.track.grouping

import charlie.laplacian.track.Track
import charlie.laplacian.track.property.Property

interface TrackGroupingMethod {
    fun getName(): String
    fun getTracks(): List<Track>
    fun addTrack(track: Track)
    fun removeTrack(track: Track)
    fun canDuplicate(): Boolean
    fun count(): Int
    fun getProperties(): List<Property<*>>
}