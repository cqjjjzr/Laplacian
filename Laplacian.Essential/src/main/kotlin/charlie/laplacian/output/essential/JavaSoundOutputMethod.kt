package charlie.laplacian.output.essential

import charlie.laplacian.output.*
import java.nio.charset.Charset
import java.util.*
import javax.sound.sampled.*
import javax.sound.sampled.FloatControl.Type.MASTER_GAIN
import kotlin.collections.plusAssign
import kotlin.experimental.and

class JavaSoundOutputMethod : OutputMethod {
    override fun init() {}

    override fun destroy() {}

    override fun openDevice(outputSettings: OutputSettings): OutputDevice
            = JavaSoundOutputDevice(outputSettings)

    override fun getMetadata(): OutputMethodMetadata = JavaSoundMetadata

    override fun getDeviceInfos(): Array<out OutputDeviceInfo> =
            AudioSystem.getMixerInfo()
                    .map { JavaSoundOutputDeviceInfo(AudioSystem.getMixer(it)) }
                    .filter { it.getAvailableSettings().isNotEmpty() }
                    .toTypedArray()
}

class JavaSoundOutputDevice(outputSettings: OutputSettings): OutputDevice {
    private var audioFormat: AudioFormat? = null

    override fun openLine(): OutputLine {
        return JavaSoundChannel(audioFormat!!)
    }

    override fun closeLine(channel: OutputLine) {
        if (channel !is JavaSoundChannel) throw ClassCastException()
        channel.pause()
        channel.dataLine.drain()
        channel.dataLine.close()
    }

    init {
        AudioSystem.getMixerInfo()

        audioFormat = AudioFormat(
                outputSettings.sampleRateHz,
                outputSettings.bitDepth,
                outputSettings.numChannels,
                true,
                false)

    }
}

class JavaSoundOutputDeviceInfo(private val mixer: Mixer): OutputDeviceInfo {
    override fun getName(): String = javaSoundRecoverMessyCode(mixer.mixerInfo.name)

    override fun getAvailableSettings(): Array<OutputSettings> {
        val list = ArrayList<OutputSettings>(mixer.sourceLineInfo.size)
        mixer.sourceLineInfo.forEach {
            AudioSystem.getLine(it).apply {
                if (this is SourceDataLine)
                    (this.lineInfo as DataLine.Info).formats
                            .filter { !it.isBigEndian }
                            .filter { it.encoding == AudioFormat.Encoding.PCM_SIGNED }
                            .filter { it.sampleSizeInBits != 24 }
                            .filter { it.sampleRate > 0 }
                            .forEach {
                                list += OutputSettings(it.sampleRate, it.sampleSizeInBits, it.channels)
                            }
            }
        }
        return list.toTypedArray()
    }
}

private val HIGH_IDENTIFY = 0b11000000.toByte()
fun javaSoundRecoverMessyCode(string: String): String {
    return String(ArrayList<Byte>().apply buf@ {
        string.toByteArray().apply {
            var i = 0
            while (i < this.size) {
                if ((this[i] and HIGH_IDENTIFY) == HIGH_IDENTIFY) {
                    this@buf += (this[i].toInt() shl 6 or (this[i + 1].toInt() shl 2 ushr 2)).toByte()
                    i++
                } else {
                    this@buf += this[i]
                }
                i++
            }
        }
    }.toByteArray(), Charset.forName(System.getProperty("file.encoding")))
}

object JavaSoundMetadata: OutputMethodMetadata {
    override fun getName(): String = "Java Sound"

    override fun getVersion(): String = "rv1"

    override fun getVersionID(): Int = 1
}

class JavaSoundChannel(audioFormat: AudioFormat) : OutputLine {
    private val BUFFER_SIZE = 1024
    internal val dataLine: SourceDataLine
    private val dataLineInfo: DataLine.Info
    private var volumeController: JavaSoundVolumeController

    override fun mix(pcmData: ByteArray, offset: Int, length: Int) {
        dataLine.write(pcmData, offset, length)
    }

    override fun getVolumeController(): VolumeController = volumeController
    override fun setVolumeController(volumeController: VolumeController) {
        if (volumeController is JavaSoundVolumeController)
            this.volumeController = volumeController
        else throw ClassCastException()
    }

    override fun open() {
        dataLine.drain()
        dataLine.start()
    }

    override fun pause() {
        dataLine.stop()
    }

    init {
        dataLineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat, BUFFER_SIZE)
        dataLine = AudioSystem.getLine(dataLineInfo) as SourceDataLine

        dataLine.open(audioFormat)
        volumeController = JavaSoundVolumeController()
        //audioFormat.getProperty()
    }

    inner class JavaSoundVolumeController: VolumeController {
        private val volumeControl: FloatControl = dataLine.getControl(MASTER_GAIN) as FloatControl

        override fun max(): Float = volumeControl.maximum

        override fun min(): Float = volumeControl.minimum

        override fun current(): Float = volumeControl.value

        override fun set(value: Float) {
            volumeControl.value = value
        }
    }
}
