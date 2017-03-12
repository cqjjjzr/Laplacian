package charlie.laplacian.track

import charlie.laplacian.source.TrackSourceInfo
import charlie.laplacian.track.extension.ExtensionParam
import charlie.laplacian.track.property.Property
import java.io.Serializable
import java.util.*

class Track(private val uuid: UUID, private val sourceInfo: TrackSourceInfo,
            properties: List<Property>, extensionParams: List<ExtensionParam>) : Serializable {
    private val properties: MutableList<Property> = LinkedList()
    private val extensionParams: MutableList<ExtensionParam> = LinkedList()

    init {
        this.properties.addAll(properties)
        this.extensionParams.addAll(extensionParams)
    }
}