package charlie.laplacian.plugin

interface Plugin {
    fun getMetadata(): PluginMetadata

    fun globalInit()
    fun init()
    fun destroy()
}

interface PluginMetadata {
    fun getName(): String
    fun getDescriptor(): String

    fun getVersion(): String
    fun getVersionID(): Int

    fun getAuthor(): String
}
