package charlie.laplacian.launcher

import charlie.laplacian.config.Configuration
import charlie.laplacian.frontend.FrontendRegistry
import charlie.laplacian.plugin.PluginInitializer

fun main(args: Array<String>) {
    initAll()
    startupGUI()
}

private fun initAll() {
    Configuration.init()
    PluginInitializer.load()
}

private fun startupGUI() {
    FrontendRegistry.startupGUI(Configuration.getFrontend())
}