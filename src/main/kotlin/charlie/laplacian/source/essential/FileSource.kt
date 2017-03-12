package charlie.laplacian.source.essential

import charlie.laplacian.I18n
import charlie.laplacian.decoder.TrackStream
import charlie.laplacian.decoder.essential.FileTrackStream
import charlie.laplacian.source.*
import java.io.File
import java.io.FileNotFoundException

class FileSource: LocalTrackSource {
    private val MEMORY_BUFFER_THRESHOLD: Long = 75 * 1024 * 1024 // 75 MB

    override fun getName(): String = I18n.getString("source.essentials.fileSource.name")

    override fun getDescription(): String = I18n.getString("source.essentials.fileSource.description")

    override fun streamFrom(source: TrackSourceInfo): TrackStream {
        if (source is FileTrackSourceInfo) {
            return source.getPath().run {
                if (!exists()) throw ExpiredLocalTrackStreamInfo(FileNotFoundException())
                if (!isFile) throw ExpiredLocalTrackStreamInfo()

                FileTrackStream(toPath())
            }
        } else {
            throw InvalidTrackSourceInfoException()
        }
    }
}

class FileTrackSourceInfo(private val path: File): TrackSourceInfo {
    override fun getSourceClass(): Class<out TrackSource> = FileSource::class.java

    fun getPath(): File {
        return path
    }
}
