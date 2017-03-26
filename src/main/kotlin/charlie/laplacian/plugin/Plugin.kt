package charlie.laplacian.plugin

interface Plugin {
    fun getMetadata(): PluginMetadata

    fun init()
    fun destroy()
}

interface PluginMetadata {
    fun getName(): String
    fun getVersion(): String
    fun getVersionID(): Int

    fun getAuthor(): String
}
