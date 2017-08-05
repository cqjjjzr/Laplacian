package charlie.laplacian.track.property

import java.io.Serializable
import java.util.*

abstract class Property<out T>: Serializable {
    abstract fun getDisplayName(): String
    abstract fun getValue(): T
    abstract fun getApplicableFor(): Set<PropertyApplicableType>
    abstract fun isStringConvertible(): Boolean

    override fun equals(other: Any?): Boolean =
            other != null && this.javaClass.isAssignableFrom(other.javaClass)
                    && this.getValue() == (other as Property<*>).getValue()

    private val hashCode by lazy {
        var result = super.hashCode()
        result = result * 31 + getDisplayName().hashCode()
        result = result * 31 + getValue()!!.hashCode()
        result = result * 31 + getApplicableFor().hashCode()
        result
    }
    override fun hashCode(): Int = hashCode
    override fun toString(): String {
        val value: T = getValue()
        if (value is Array<*>) {
            return if (value.size == 0) ""
            else if (value.size == 1) value[0].toString()
            else Arrays.toString(value)
        }
        return value.toString()
    }
}

abstract class PropertyImpl<out T>(private val propValue: T,
                                   private val propDisplayName: String,
                                   private val applicableTypes: Set<PropertyApplicableType>,
                                   private val isPropStringConvertible: Boolean = true): Property<T>() {
    override fun getDisplayName(): String = propDisplayName
    override fun getValue(): T = propValue
    override fun getApplicableFor(): Set<PropertyApplicableType> = applicableTypes
    override fun isStringConvertible(): Boolean = isPropStringConvertible
}

enum class PropertyApplicableType {
    TRACK, GROUPING_METHOD
}

// FOR OPTIMIZE
class PropertyApplicableTypeSets {
    companion object {
        @JvmStatic
        val TRACK_ONLY: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK))
        @JvmStatic
        val GROUPING_ONLY: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK))
        @JvmStatic
        val TRACK_AND_GROUPING: Set<PropertyApplicableType> = Collections.unmodifiableSet(EnumSet.of(PropertyApplicableType.TRACK, PropertyApplicableType.GROUPING_METHOD))
    }
}
