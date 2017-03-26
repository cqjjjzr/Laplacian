package charlie.laplacian.plugin

import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

class PluginRegistry {
    private val plugins: MutableMap<ClassLoader, Plugin> = LinkedHashMap()

    private fun loadPluginList() {
        File("plugins").apply {
            if (!exists() || !isDirectory)
                mkdirs()

            Files.walkFileTree(toPath(), object: SimpleFileVisitor<Path>() {
                override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
                    return super.postVisitDirectory(dir, exc)
                }

                override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    return super.visitFile(file, attrs)
                }

                override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    return super.preVisitDirectory(dir, attrs)
                }

                override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                    return super.visitFileFailed(file, exc)
                }
            })
        }
    }

    private fun unregisterAndRemove(key: ClassLoader, value: Plugin) {
        value.destroy()
        plugins -= key
    }
}