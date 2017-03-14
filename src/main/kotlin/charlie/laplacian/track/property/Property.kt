package charlie.laplacian.track.property

import java.io.Serializable
import java.util.*

abstract class Property: Serializable {
    abstract fun getName(): String
    abstract fun getValue(): Any
    abstract fun setValue(value: Any)

    abstract fun getType(): PropertyType
    abstract fun getApplicableFor(): Set<PropertyApplicableType>
    abstract fun canDuplicate(): Boolean

    override fun equals(other: Any?): Boolean = other != null && this.javaClass.isAssignableFrom(other.javaClass)
    override fun hashCode(): Int = 31 * getName().hashCode() * getValue().hashCode() * getType().hashCode()
    override fun toString(): String = getValue().toString()
}

enum class PropertyApplicableType {
    TRACK, GROUPING_METHOD
}

enum class PropertyType {
    STRING, NUMBER, IMAGE, PROPERTY_ITEM, ALIAS, REFERENCE
}

// FOR OPTIMIZE
val PROPERTY_APPLICABLE_TRACK_ONLY: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK))

val PROPERTY_APPLICABLE_GROUPING_ONLY: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK))

val PROPERTY_APPLICABLE_TRACK_AND_GROUPING: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK, PropertyApplicableType.GROUPING_METHOD))