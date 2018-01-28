package charlie.laplacian.decoder.essential

import charlie.laplacian.config.Configuration
import charlie.laplacian.config.ConfigurationDefaultValues
import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import charlie.laplacian.playctrl.OutputHelper
import charlie.laplacian.source.SourceRegistry
import charlie.laplacian.source.essential.FileSource
import charlie.laplacian.source.essential.FileTrackSourceInfo
import org.junit.Test
import java.io.File
import java.lang.Thread.sleep
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine


class FFmpegDecoderSessionTest {
    @Test
    fun test() {
        FFmpegDecoder.init()
        Configuration.init()
        ConfigurationDefaultValues.refresh()
        SourceRegistry.registerSource(FileSource())
        OutputMethodRegistry.registerOutputMethod(JavaSoundOutputMethod())
        DecoderRegistry.registerDecoderFactory(FFmpegDecoder())

        OutputHelper(FileTrackSourceInfo(
                File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a"))).apply {
            play()
            sleep(10000)
            println("$positionMillis / $durationMillis")
            seek(0)
            sleep(10000)
            close()
        }
    }

    @Test
    fun mixerInfo() {
        JavaSoundOutputMethod().getDeviceInfos().forEach {
            println(it.getName())
        }
    }

    @Test
    fun mixerInfo2() {
        AudioSystem.getMixerInfo().forEach {
            println("info: " + it.toString())
            AudioSystem.getMixer(it).apply {
                println("output " + this)
                sourceLineInfo.forEach {
                    println("    info: " + it.toString())
                    AudioSystem.getLine(it).apply {
                        if (this is SourceDataLine) {
                            (this.lineInfo as DataLine.Info).formats.forEach {
                                println("    format: " + format.toString())
                            }
                        }
                    }
                }
            }
            println()
        }
    }
}