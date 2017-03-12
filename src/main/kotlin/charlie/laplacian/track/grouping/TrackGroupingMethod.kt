package charlie.laplacian.track.grouping

import charlie.laplacian.track.Track
import charlie.laplacian.track.property.Property

interface TrackGroupingMethod {
    fun getTracks(): List<Track>
    fun getProperties(): List<Property>
}