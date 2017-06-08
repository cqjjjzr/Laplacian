package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import charlie.laplacian.track.property.PropertyType
import java.awt.Image

class ArtworkProperty(private var name: String,
                      private var artworks: MutableList<Artwork>): Property() {
    override fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    override fun getValue(): Any = artworks

    @Suppress("unchecked_cast")
    override fun setValue(value: Any) {
        if (value !is MutableList<*>) throw ClassCastException()
        this.artworks = value as MutableList<Artwork>
    }

    override fun getType(): PropertyType = PropertyType.LIST

    override fun getApplicableFor(): Set<PropertyApplicableType> = TRACK_AND_GROUPING
}

data class Artwork(var name: String,
                   val artwork: Image)