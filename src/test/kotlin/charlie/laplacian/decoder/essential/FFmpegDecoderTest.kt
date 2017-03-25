package charlie.laplacian.decoder.essential

import charlie.laplacian.mixer.essential.JavaSoundMixer
import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import org.junit.Test
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine


class FFmpegDecoderTest {
    @Test
    fun test() {
        AudioSystem.getMixerInfo().forEach {
            println("info: " + it)
            AudioSystem.getMixer(it).apply {
                println("mixer " + this)
                sourceLineInfo.forEach {
                    println("    info: " + it)
                    AudioSystem.getLine(it).apply {
                        if (this is SourceDataLine) {
                            (this.lineInfo as DataLine.Info).formats.forEach {
                                println("    format: " + format)
                            }
                        }
                    }
                }
            }
            println()
        }
        FFmpegDecoder.init()
        FFmpegDecoderFactory()
                .getDecoder(JavaSoundMixer().apply {
                    init(44100f, 16, 2)
                }, 44100f, 16, 2, FileSource().streamFrom(
                        FileTrackSourceInfo(
                                File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a")))).apply {
            play()
            synchronized(this) {
                (this as Object).wait()
            }
        }
    }
}