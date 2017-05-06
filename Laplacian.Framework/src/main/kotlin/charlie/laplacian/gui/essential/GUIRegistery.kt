package charlie.laplacian.gui.essential

import java.util.*

object GUIRegistery {
    private val guis: MutableSet<GUISupport> = HashSet()

    fun registerGUI(gui: GUISupport) {
        guis += gui
    }

    fun unregisterGUI(gui: GUISupport) {
        guis -= gui
    }

    fun findGUI(className: String) = guis.find { it.javaClass.name == className } ?: throw NoSuchElementException(className)

    fun getGUIs(): Set<GUISupport> = Collections.unmodifiableSet(guis)

    private var currentGUI: GUISupport? = null

    fun startupGUI(gui: GUISupport) {
        gui.init()
        currentGUI?.destroy()
        currentGUI = gui
    }
}