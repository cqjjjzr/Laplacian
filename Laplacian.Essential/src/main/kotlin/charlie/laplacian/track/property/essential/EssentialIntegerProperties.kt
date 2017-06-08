package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_AND_GROUPING
import charlie.laplacian.track.property.PropertyApplicableTypeSets.Companion.TRACK_ONLY
import charlie.laplacian.track.property.PropertyType

abstract class IntegerTypeProperty(private var value: Int,
                                   private val i18NKey: String,
                                   private val applicableTypes: Set<PropertyApplicableType>): Property() {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun setValue(value: Any) {
        if (value !is Number)
            throw ClassCastException()
        this.value = value.toInt()
    }

    override fun getType(): PropertyType = PropertyType.NUMBER

    override fun getApplicableFor(): Set<PropertyApplicableType> = applicableTypes
}

class TrackNumberProperty(value: Int): IntegerTypeProperty(value, "property.essential.trackNumberProperty.name",
        TRACK_ONLY)
class ReleaseYearProperty(value: Int): IntegerTypeProperty(value, "property.essential.releaseYearProperty.name",
        TRACK_AND_GROUPING)
class BPMProperty(value: Int): IntegerTypeProperty(value, "property.essential.bpmProperty.name",
        TRACK_ONLY)