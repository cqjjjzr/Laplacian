package charlie.laplacian.plugin

import java.awt.Image

interface Plugin {
    fun getMetadata(): PluginMetadata

    fun init()
    fun destroy()
}

interface PluginMetadata {
    fun getName(): String
    fun getDescriptor(): String

    fun getVersion(): String
    fun getVersionID(): Int

    fun getAuthor(): String
    fun getIcon(): Image?
}
