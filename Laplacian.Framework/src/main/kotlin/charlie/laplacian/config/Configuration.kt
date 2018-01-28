package charlie.laplacian.config

import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.decoder.DecoderSession
import charlie.laplacian.frontend.Frontend
import charlie.laplacian.frontend.FrontendRegistry
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import org.apache.commons.configuration2.XMLConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import java.nio.file.Files
import java.nio.file.Paths
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

object Configuration {
    private val FILE_NAME = "Laplacian.xml"

    private lateinit var conf: XMLConfiguration

    fun getDecoder(stream: TrackStream): DecoderSession = DecoderRegistry.tryDecode(getOutputSettings(), stream)

    fun getOutputSettings() = OutputSettings(
            conf.getFloat("output.sampleRateHz", ConfigurationDefaultValues.mixerSampleRateHz),
            conf.getInt("output.bitDepth", ConfigurationDefaultValues.bitDepth),
            conf.getInt("output.numChannels", ConfigurationDefaultValues.numChannels)
    )

    fun getOutputDevice(outputSettings: OutputSettings) =
            OutputMethodRegistry
            .getOutputMethod(conf.getString("output.mixer", ConfigurationDefaultValues.outputMethodClassName))
            .openDevice(outputSettings)

    fun getFrontend(): Frontend = FrontendRegistry.findFrontend(conf.getString("frontend.guiSupport", ConfigurationDefaultValues.frontendClassName))

    fun init() {
        Paths.get(FILE_NAME).apply {
            if (!Files.exists(this))
                Files.createFile(this)
        }
        conf = Configurations().xml(FILE_NAME)
    }
}

object ConfigurationDefaultValues {
    val outputMethodClassName = "charlie.laplacian.output.essential.JavaSoundOutputMethod"
    var mixerSampleRateHz: Float = 44100f
    var bitDepth: Int = 16
    var numChannels: Int = 2
    val frontendClassName = "charlie.laplacian.frontend.essential.MaterialGUI" // Oops @ice1000

    fun refresh() {
        AudioSystem.getMixerInfo().forEach {
            AudioSystem.getMixer(it).apply {
                sourceLineInfo.forEach {
                    AudioSystem.getLine(it).apply {
                        if (this is SourceDataLine) {
                            (this.lineInfo as DataLine.Info).formats.forEach {
                                if (!format.isBigEndian)
                                    if (format.sampleSizeInBits != 24)
                                        if (format.encoding == AudioFormat.Encoding.PCM_SIGNED)
                                            if (format.sampleSizeInBits > 0) {
                                                mixerSampleRateHz = format.sampleRate
                                                bitDepth = format.sampleSizeInBits
                                                numChannels = format.channels
                                                return
                                            }
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        refresh()
    }
}