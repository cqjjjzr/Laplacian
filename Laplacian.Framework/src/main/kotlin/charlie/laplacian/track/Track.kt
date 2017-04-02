package charlie.laplacian.track

import charlie.laplacian.source.TrackSourceInfo
import charlie.laplacian.track.extension.ExtensionParam
import charlie.laplacian.track.property.Property
import charlie.laplacian.track.property.PropertyApplicableType
import java.io.Serializable
import java.util.*

class Track(val uuid: UUID, val sourceInfo: TrackSourceInfo,
            properties: List<Property>, extensionParams: List<ExtensionParam>) : Serializable {
    private val properties: MutableList<Property> = LinkedList()
    private val extensionParams: MutableList<ExtensionParam> = LinkedList()

    init {
        this.properties.addAll(properties)
        this.extensionParams.addAll(extensionParams)
    }

    fun getProperties(): MutableList<Property> = Collections.unmodifiableList(properties)
    fun addProperty(property: Property) {
        if (!property.getApplicableFor().contains(PropertyApplicableType.TRACK))
            throw IllegalArgumentException("not applicable to track!")
        if (!property.canDuplicate())
            if (properties.filter { it.javaClass.isAssignableFrom(property.javaClass) }.isNotEmpty())
                throw IllegalArgumentException("property can't be duplicate!")
        properties += property
    }

    fun removeProperty(property: Property) {
        properties -= property
    }

    override fun equals(other: Any?): Boolean = if (other != null && other is Track) other.uuid == uuid else false
    override fun hashCode(): Int = uuid.hashCode()
}