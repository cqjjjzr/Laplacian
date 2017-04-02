package charlie.laplacian.plugin

import ro.fortsoft.pf4j.DefaultPluginManager
import ro.fortsoft.pf4j.PluginManager
import java.nio.file.Paths

object PluginInitializer {
    private val FILE_PATH = "plugins/"
    private val pluginManager: PluginManager = DefaultPluginManager(Paths.get(FILE_PATH))

    init {
        pluginManager.loadPlugins()
        pluginManager.startPlugins()
    }

    fun unload() {
        pluginManager.stopPlugins()
    }
}