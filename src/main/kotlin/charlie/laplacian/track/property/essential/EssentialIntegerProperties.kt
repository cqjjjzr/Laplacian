package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.*

abstract class IntegerTypeProperty(private var value: Int,
                                   private val i18NKey: String,
                                   private val applicableTypes: Set<PropertyApplicableType>,
                                   private val canDuplicate: Boolean): Property() {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun setValue(value: Any) {
        if (value !is Number)
            throw ClassCastException()
        this.value = value.toInt()
    }

    override fun getType(): PropertyType = PropertyType.NUMBER

    override fun getApplicableFor(): Set<PropertyApplicableType> = applicableTypes

    override fun canDuplicate(): Boolean = canDuplicate
}

class TrackNumberProperty(value: Int): IntegerTypeProperty(value, "property.essential.trackNumberProperty.name",
        PROPERTY_APPLICABLE_TRACK_ONLY, false)
class ReleaseYearProperty(value: Int): IntegerTypeProperty(value, "property.essential.releaseYearProperty.name",
        PROPERTY_APPLICABLE_TRACK_AND_GROUPING, false)
class BPMProperty(value: Int): IntegerTypeProperty(value, "property.essential.bpmProperty.name",
        PROPERTY_APPLICABLE_TRACK_ONLY, false)