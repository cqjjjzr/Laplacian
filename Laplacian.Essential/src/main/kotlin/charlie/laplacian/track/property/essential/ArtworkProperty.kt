package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import java.awt.Image

class ArtworkProperty(artworks: Array<Artwork>)
    : I18nProperty<Array<Artwork>>(artworks, "property.essential.artworkProperty.name", TRACK_AND_GROUPING, false)

data class Artwork(var name: String,
                   val artwork: Image)