package charlie.laplacian.output

interface OutputLine {
    fun mix(pcmData: ByteArray, offset: Int, length: Int)
    fun getVolumeController(): VolumeController
    fun setVolumeController(volumeController: VolumeController)

    fun open()
    fun pause()
}