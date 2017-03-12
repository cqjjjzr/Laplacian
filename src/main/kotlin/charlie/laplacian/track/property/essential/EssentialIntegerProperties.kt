package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyType

abstract class IntegerTypeProperty(private val value: Int, private val i18NKey: String): Property {
    override fun getName(): String = I18n.getString(i18NKey)

    override fun getValue(): Any = value

    override fun getType(): PropertyType = PropertyType.NUMBER
}

class TrackNumberProperty(value: Int): IntegerTypeProperty(value, "property.essential.trackNumberProperty.name")
class ReleaseYearProperty(value: Int): IntegerTypeProperty(value, "property.essential.releaseYearProperty.name")
class BPMProperty(value: Int): IntegerTypeProperty(value, "property.essential.bpmProperty.name")