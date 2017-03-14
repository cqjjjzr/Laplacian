package charlie.laplacian.track.property.essential

import charlie.laplacian.track.property.PROPERTY_APPLICABLE_TRACK_AND_GROUPING
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyType
import java.awt.Image

class ArtworkProperty(private var name: String,
                      private var artwork: Image): Property() {
    override fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    override fun getValue(): Any = artwork

    override fun setValue(value: Any) {
        if (value !is Image) throw ClassCastException()
        this.artwork = value
    }

    override fun getType(): PropertyType = PropertyType.IMAGE

    override fun getApplicableFor(): Set<PropertyApplicableType> = PROPERTY_APPLICABLE_TRACK_AND_GROUPING

    override fun canDuplicate(): Boolean = true
}