package charlie.laplacian.mixer

interface AudioChannel {
    fun mix(pcmData: ByteArray, offset: Int, length: Int)
    fun getVolumeController(): VolumeController
    fun setVolumeController(volumeController: VolumeController)

    fun open()
    fun pause()
}