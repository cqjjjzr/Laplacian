package charlie.laplacian.track.extension

import charlie.laplacian.plugin.Plugin
import java.io.Serializable

interface ExtensionParam: Serializable {
    fun getPlugin(): Plugin
}