package charlie.laplacian.config

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import charlie.laplacian.stream.TrackStream
import org.apache.commons.configuration2.XMLConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine

object Configuration {
    private val FILE_NAME = "Laplacian.xml"

    private val conf: XMLConfiguration

    fun getDecoder(stream: TrackStream): Decoder =
            OutputSettings(
                    conf.getFloat("output.sampleRateHz", ConfigurationDefaultValues.mixerSampleRateHz),
                    conf.getInt("output.bitDepth", ConfigurationDefaultValues.bitDepth),
                    conf.getInt("output.numChannels", ConfigurationDefaultValues.numChannels)
            ).run {
                DecoderRegistry.tryDecode(
                        OutputMethodRegistry
                                .getOutputMethod(conf.getString("output.defaultMixer", ConfigurationDefaultValues.mixerClassName))
                                .openDevice(this), this, stream)
            }

    init {
        conf = Configurations().xml(FILE_NAME)
    }
}

object ConfigurationDefaultValues {
    val mixerClassName = JavaSoundOutputMethod::javaClass.name
    var mixerSampleRateHz: Float = 44100f
    var bitDepth: Int = 16
    var numChannels: Int = 2

    fun refresh() {
        (AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]).sourceLineInfo[0] as DataLine.Info)
                .formats[0].apply {
            mixerSampleRateHz = sampleRate
            bitDepth = sampleSizeInBits
            numChannels = channels
        }
    }

    init {
        refresh()
    }
}