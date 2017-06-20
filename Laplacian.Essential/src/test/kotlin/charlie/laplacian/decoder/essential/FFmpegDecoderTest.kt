package charlie.laplacian.decoder.essential

import charlie.laplacian.config.Configuration
import charlie.laplacian.config.ConfigurationDefaultValues
import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import charlie.laplacian.output.essential.javaSoundRecoverMessyCode
import charlie.laplacian.playctrl.OutputHelper
import charlie.laplacian.plugin.PluginInitializer
import charlie.laplacian.source.SourceRegistry
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
            println("info: " + javaSoundRecoverMessyCode(it.toString()))
            AudioSystem.getMixer(it).apply {
                println("output " + this)
                sourceLineInfo.forEach {
                    println("    info: " + javaSoundRecoverMessyCode(it.toString()))
                    AudioSystem.getLine(it).apply {
                        if (this is SourceDataLine) {
                            (this.lineInfo as DataLine.Info).formats.forEach {
                                println("    format: " + javaSoundRecoverMessyCode(format.toString()))
                            }
                        }
                    }
                }
            }
            println()
        }
        FFmpegDecoder.init()
        /*OutputSettings(44100f, 16, 2).apply {
            FFmpegDecoderFactory()
                    .getDecoder(JavaSoundOutputMethod().openDevice(this), this, FileSource().streamFrom(
                            )).apply {
                play()
                Thread.sleep(10000)
                close()
            }
        }*/
        Configuration.init()
        ConfigurationDefaultValues.refresh()
        SourceRegistry.registerSource(FileSource())
        OutputMethodRegistry.registerOutputMethod(JavaSoundOutputMethod())
        DecoderRegistry.registerDecoderFactory(FFmpegDecoderFactory())
        OutputHelper(FileTrackSourceInfo(
                File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a"))).play()
        while (true) ;
    }

    @Test
    fun mixerInfo() {
        JavaSoundOutputMethod().getDeviceInfos().forEach {
            println(it.getName())
        }
    }

    @Test
    fun test2() {
        PluginInitializer
    }
}