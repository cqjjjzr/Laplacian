package charlie.laplacian.plugin

/*
object PluginRegistry {
    private val logger: Logger = LogManager.getLogger("laplacian.pluginRegistry")
    private val plugins: MutableMap<ClassLoader, Any> = LinkedHashMap()

    private fun loadPluginList() {
        logger.infoI18n("pluginRegistry.enterLoad")

        File("plugins").apply {
            if (!exists() || !isDirectory)
                mkdirs()

            Files.walkFileTree(toPath(), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (file.fileName.endsWith(".jar")) {
                        loadPlugin(file)
                    }

                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    private fun loadPlugin(file: Path) {
        URLClassLoader(Collections.singleton(file.toUri().toURL()).toTypedArray()).apply classloader@{
            getResourceAsStream("l_plugin.properties")?.apply stream@{
                Properties().apply {
                    load(this@stream)
                    getProperty("class")?.apply {
                        startupClassLoader(file.fileName.toString(), this, this@classloader)

                    } ?: logger.warnFormatI18n("pluginRegistry.manifest.missingClassEntry", file.fileName)
                }
            } ?: logger.warnFormatI18n("pluginRegistry.manifest.noManifest", file.fileName)
        }
    }

    private fun startupClassLoader(filename: String, className: String, classLoader: ClassLoader) {
        try {
            classLoader.loadClass(className).newInstance().apply {
                if (this !is Plugin)
                    logger.warnFormatI18n("pluginRegistry.classNotPlugin", filename)
                else
                    startupPlugin(this, filename, className, classLoader)
            }
        } catch(e: ClassNotFoundException) {
            logger.warnFormatI18n("pluginRegistry.classNotFound", filename, className)
        } catch (e: InstantiationException) {
            logger.logException(e, I18n.format("pluginRegistry.illegalClass", filename, className), Level.WARN)
        } catch (e: IllegalAccessException) {
            logger.logException(e, I18n.format("pluginRegistry.illegalClass", filename, className), Level.WARN)
        }
    }

    private fun startupPlugin(plugin: Plugin, filename: String, className: String, classLoader: ClassLoader) {
        try {
            plugin.init()
            plugins[classLoader] = plugin
        } catch(e: Exception) {
            logger.logException(e, I18n.format("pluginRegistry.initException", filename, className), Level.WARN)
        }
    }

    private fun destroy() {
        plugins.forEach {
            try {
                it.value.destroy()
            } catch(e: Exception) {
                logger.logException(e, I18n.format("pluginRegistry.destroyException", it.value.javaClass), Level.WARN)
            }
        }
        plugins.clear()
    }
}*/