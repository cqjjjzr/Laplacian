package charlie.laplacian.decoder.essential

import charlie.laplacian.mixer.essential.JavaSoundMixer
import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import org.junit.Test
import java.io.File

class FFmpegDecoderTest {
    @Test
    fun test() {
        FFmpegDecoder.init()
        FFmpegDecoderFactory()
                .getDecoder(JavaSoundMixer().apply {
                    init(44100f, 24, 2)
                }, 44100f, 24, 2, FileSource().streamFrom(
                        FileTrackSourceInfo(
                                File("E:\\iTunes\\iTunes Media\\Music\\动点p\\三月雨\\01 三月雨 (feat. 乐正绫).mp3")))).apply {
            play()
            synchronized(this) {
                (this as Object).wait()
            }
        }
    }
}