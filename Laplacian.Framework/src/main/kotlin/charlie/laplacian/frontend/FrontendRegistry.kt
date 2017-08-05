package charlie.laplacian.frontend

import java.util.*

object FrontendRegistry {
    private val frontends: MutableSet<Frontend> = HashSet()

    fun registerFrontend(gui: Frontend) {
        frontends += gui
    }

    fun unregisterFrontend(gui: Frontend) {
        frontends -= gui
    }

    fun findFrontend(className: String) = frontends.find { it.javaClass.name == className } ?: throw NoSuchElementException(className)

    fun getFrontends(): Set<Frontend> = Collections.unmodifiableSet(frontends)

    private var currentFrontend: Frontend? = null

    fun startupGUI(frontend: Frontend) {
        currentFrontend?.destroy()
        currentFrontend = frontend
        frontend.init()
    }
}