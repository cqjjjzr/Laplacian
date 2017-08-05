package charlie.laplacian.source.essential

import charlie.laplacian.I18n
import charlie.laplacian.source.*
import charlie.laplacian.stream.TrackStream
import charlie.laplacian.stream.essential.FileTrackStream
import java.io.File
import java.io.FileNotFoundException

class FileSource: LocalTrackSource {
    private val MEMORY_BUFFER_THRESHOLD: Long = 75 * 1024 * 1024 // 75 MB

    override fun getMetadata(): TrackSourceMetadata = FileSourceMetadata

    override fun streamFrom(source: TrackSourceInfo): TrackStream {
        if (source is FileTrackSourceInfo) {
            return source.getPath().run {
                if (!exists()) throw ExpiredLocalTrackStreamInfo(FileNotFoundException())
                if (!isFile) throw ExpiredLocalTrackStreamInfo()

                charlie.laplacian.stream.essential.FileTrackStream(toPath())
            }
        } else {
            throw InvalidTrackSourceInfoException()
        }
    }

    object FileSourceMetadata: TrackSourceMetadata {
        override fun getName(): String = I18n.getString("source.essential.fileSource.name")

        override fun getDescription(): String = I18n.getString("source.essential.fileSource.description")

        override fun getVersion(): String = "rv1"

        override fun getVersionID(): Int = 1

        override fun getSupportedTrackInfos(): Array<Class<out TrackSourceInfo>> =
                Array(1, { FileTrackSourceInfo::class.java })
    }
}

class FileTrackSourceInfo(private val path: File): TrackSourceInfo {
    override fun getSourceClass(): Class<out TrackSource> = FileSource::class.java

    fun getPath(): File {
        return path
    }
}
