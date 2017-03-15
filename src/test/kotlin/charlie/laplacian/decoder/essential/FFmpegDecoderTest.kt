package charlie.laplacian.decoder.essential

import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import io.humble.video.customio.URLProtocolManager
import org.junit.Test
import java.io.File

class FFmpegDecoderTest {
    @Test
    fun test() {
        Class.forName("io.humble.video.Demuxer")
        URLProtocolManager.getManager().registerFactory("laplacian", HumbleVideoProtocolHandlerFactory())
        println(FFmpegDecoderFactory()
                .getDecoder(FileSource().streamFrom(
                        FileTrackSourceInfo(
                                File("C:\\Users\\cqjjj\\IdeaProjects\\Laplacian\\src\\test\\resources\\testaudio.mp3"))))
                .positionMillis())
    }
}