package charlie.laplacian.stream.essential

import charlie.laplacian.stream.SeekableTrackStream
import charlie.laplacian.stream.SizeKnownTrackStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileTrackStream(private val path: Path) : SeekableTrackStream, SizeKnownTrackStream {
    private val buffer: MappedByteBuffer
    private val channel: FileChannel = FileChannel.open(path, StandardOpenOption.READ)

    override fun close() {
        channel.close()
    }

    override fun position(): Int = buffer.position()

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        val readLength: Int = Math.min(this.buffer.remaining(), length)
        if (readLength == 0) return -1
        this.buffer[buffer, offset, readLength]
        return readLength
    }

    override fun seek(positionBytes: Int) {
        buffer.position(positionBytes)
    }

    override fun size(): Int = channel.size().toInt()

    override fun forceSeek(positionBytes: Int) {
        seek(positionBytes)
    }

    fun getPath(): Path = path

    init {
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
    }
}