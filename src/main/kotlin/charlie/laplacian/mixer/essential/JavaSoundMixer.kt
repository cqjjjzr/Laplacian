package charlie.laplacian.mixer.essential

import charlie.laplacian.decoder.VolumeController
import charlie.laplacian.mixer.AudioChannel
import charlie.laplacian.mixer.Mixer
import javax.sound.sampled.*
import javax.sound.sampled.FloatControl.Type.MASTER_GAIN

class JavaSoundMixer: Mixer {
    private var audioFormat: AudioFormat? = null

    override fun init(sampleRateHz: Float, bitDepth: Int, numChannel: Int) {
        audioFormat = AudioFormat(sampleRateHz, bitDepth, numChannel, true, false)
    }

    override fun openChannel(): AudioChannel {
        return JavaSoundChannel(audioFormat!!)
    }

    override fun closeChannel(channel: AudioChannel) {
        if (channel !is JavaSoundChannel) throw ClassCastException()
        channel.pause()
        channel.dataLine.close()
    }
}

class JavaSoundChannel(audioFormat: AudioFormat) : AudioChannel {
    private val BUFFER_SIZE = 1024
    internal val dataLine: SourceDataLine
    private val dataLineInfo: DataLine.Info
    private val volumeController: VolumeController

    override fun mix(pcmData: ByteArray, offset: Int, length: Int) {
        dataLine.write(pcmData, offset, length)
    }

    override fun getVolumeController(): VolumeController = volumeController

    override fun open() {
        dataLine.start()
    }

    override fun pause() {
        dataLine.drain()
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
