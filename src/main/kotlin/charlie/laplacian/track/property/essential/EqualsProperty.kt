package charlie.laplacian.track.property.essential

import charlie.laplacian.I18n
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyType
import java.util.*

class EqualsProperty(private val value: UUID): Property {
    override fun getName(): String = I18n.getString("property.essential.equalsProperty.name")

    override fun getValue(): Any =  value

    override fun getType(): PropertyType = PropertyType.REFERENCE
}