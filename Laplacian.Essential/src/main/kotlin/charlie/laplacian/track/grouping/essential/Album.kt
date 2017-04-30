package charlie.laplacian.track.grouping.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.Track
import charlie.laplacian.track.grouping.TrackGroupingMethod
import charlie.laplacian.track.property.Property
import java.util.*

class Album: TrackGroupingMethod {
    private val tracks: MutableList<Track> = LinkedList()
    private val properties: MutableList<Property> = LinkedList()

    override fun getName(): String = I18n.getString("grouping.album.name")

    override fun getTracks(): List<Track> = Collections.unmodifiableList(tracks)

    override fun getProperties(): List<Property> = Collections.unmodifiableList(properties)

    override fun addTrack(track: Track) {
        if (tracks.find { it == track } != null)
            throw IllegalArgumentException("duplicate track")
        tracks += track
    }

    override fun removeTrack(track: Track) {
        tracks -= track
    }

    override fun canDuplicate(): Boolean = false

    override fun count(): Int = tracks.size
}