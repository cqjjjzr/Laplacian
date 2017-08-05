package charlie.laplacian.track

import charlie.laplacian.source.TrackSourceInfo
import charlie.laplacian.track.extension.ExtensionParam
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import java.io.Serializable
import java.util.*
import kotlin.reflect.KClass

class Track(val uuid: UUID, val sourceInfo: TrackSourceInfo,
            properties: List<Property<*>>, extensionParams: List<ExtensionParam>) : Serializable {
    private val properties: MutableSet<Property<*>> = HashSet()
    private val extensionParams: MutableList<ExtensionParam> = LinkedList()

    init {
        this.properties.addAll(properties)
        this.extensionParams.addAll(extensionParams)
    }

    fun getProperties(): Set<Property<*>> = Collections.unmodifiableSet(properties)
    fun addProperty(property: Property<*>) {
        if (!property.getApplicableFor().contains(PropertyApplicableType.TRACK))
            throw IllegalArgumentException("not applicable to track!")
        if (properties.find { it.javaClass.isAssignableFrom(property.javaClass) } != null)
            throw IllegalArgumentException("property can't be duplicate!")
        properties += property
    }

    fun removeProperty(property: Property<*>) {
        properties -= property
    }

    override fun equals(other: Any?): Boolean = if (other != null && other is Track) other.uuid == uuid else false
    override fun hashCode(): Int = uuid.hashCode()

    operator fun plus(property: Property<*>) = addProperty(property)
    operator fun minus(property: Property<*>) = removeProperty(property)
    operator fun get(propertyClassName: String) = properties
            .filter { it::class.qualifiedName == propertyClassName }
    operator fun get(propertyClass: KClass<out Property<*>>) = properties
            .filter { propertyClass.isInstance(it) }
}