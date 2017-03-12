package charlie.laplacian.track.property

import java.io.Serializable

interface Property: Serializable {
    fun getName(): String
    fun getValue(): Any
    fun getType(): PropertyType
}

enum class PropertyType {
    STRING, NUMBER, IMAGE, PROPERTY_ITEM, LIST, ALIAS, REFERENCE
}
