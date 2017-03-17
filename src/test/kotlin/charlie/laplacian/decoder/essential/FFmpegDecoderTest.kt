package charlie.laplacian.decoder.essential

import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import io.humble.video.customio.URLProtocolManager
import org.junit.Test
import java.io.File

class FFmpegDecoderTest {
    @Test
    fun test() {
        URLProtocolManager.getManager().registerFactory("laplacian", HumbleVideoProtocolHandlerFactory())
        println(FFmpegDecoderFactory()
                .getDecoder(FileSource().streamFrom(
                        FileTrackSourceInfo(
                                File("E:\\iTunes\\iTunes Media\\Music\\动点p\\三月雨\\01 三月雨 (feat. 乐正绫).mp3"))))
                // USE YOUR CUSTOM TEST AUDIO HERE
                .durationMillis())
    }
}