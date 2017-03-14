package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.PROPERTY_APPLICABLE_TRACK_ONLY
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyType
import java.util.*

class EqualsProperty(private var value: UUID): Property() {
    override fun getName(): String = I18n.getString("property.essential.equalsProperty.name")

    override fun getValue(): Any = value

    override fun setValue(value: Any) {
        if (value !is UUID) throw ClassCastException()
        this.value = value
    }

    override fun getType(): PropertyType = PropertyType.REFERENCE

    override fun getApplicableFor(): Set<PropertyApplicableType> = PROPERTY_APPLICABLE_TRACK_ONLY

    override fun canDuplicate(): Boolean = true
}