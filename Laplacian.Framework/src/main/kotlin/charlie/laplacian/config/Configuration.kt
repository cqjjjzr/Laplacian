package charlie.laplacian.config

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.gui.GUIRegistery
import charlie.laplacian.gui.GUISupport
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream
import org.apache.commons.configuration2.XMLConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine

object Configuration {
    private val FILE_NAME = "Laplacian.xml"

    private var conf: XMLConfiguration? = null

    fun getDecoder(stream: TrackStream): Decoder = DecoderRegistry.tryDecode(getOutputSettings(), stream)

    fun getOutputSettings() = OutputSettings(
            conf!!.getFloat("output.sampleRateHz", ConfigurationDefaultValues.mixerSampleRateHz),
            conf!!.getInt("output.bitDepth", ConfigurationDefaultValues.bitDepth),
            conf!!.getInt("output.numChannels", ConfigurationDefaultValues.numChannels)
    )

    fun getOutputDevice(outputSettings: OutputSettings) =
            OutputMethodRegistry
            .getOutputMethod(conf!!.getString("output.mixer", ConfigurationDefaultValues.mixerClassName))
            .openDevice(outputSettings)

    fun getGUISupport(): GUISupport = GUIRegistery.findGUI(conf!!.getString("gui.guiSupport", ConfigurationDefaultValues.guiSupportClassName))

    fun init() {
        conf = Configurations().xml(FILE_NAME)
    }
}

object ConfigurationDefaultValues {
    val mixerClassName = "charlie.laplacian.output.essential.JavaSoundOutputMethod"
    var mixerSampleRateHz: Float = 44100f
    var bitDepth: Int = 16
    var numChannels: Int = 2
    val guiSupportClassName = "charlie.laplacian.gui.essential.MaterialGUI"

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