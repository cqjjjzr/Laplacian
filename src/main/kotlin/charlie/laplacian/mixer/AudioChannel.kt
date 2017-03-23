package charlie.laplacian.mixer

import charlie.laplacian.decoder.VolumeController

interface AudioChannel {
    fun mix(pcmData: ByteArray, offset: Int, length: Int)
    fun getVolumeController(): VolumeController

    fun open()
    fun pause()
}