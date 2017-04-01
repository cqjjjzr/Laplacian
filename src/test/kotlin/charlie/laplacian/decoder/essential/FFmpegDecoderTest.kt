package charlie.laplacian.decoder.essential

import charlie.laplacian.output.OutputSettings
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import org.junit.Ignore
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
                println("output " + this)
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
        OutputSettings(44100f, 16, 2).apply {
            FFmpegDecoderFactory()
                    .getDecoder(JavaSoundOutputMethod().openDevice(this), this, FileSource().streamFrom(
                            FileTrackSourceInfo(
                                    File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a")))).apply {
                play()
                Thread.sleep(10000)
                close()
            }
        }
    }

    @Ignore
    fun mixerInfo() {
        JavaSoundOutputMethod().getDeviceInfos().forEach {
            println(it.getName())
        }
    }
}