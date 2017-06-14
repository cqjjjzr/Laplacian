package charlie.laplacian.output

interface OutputLine {
    fun mix(pcmData: ByteArray, offset: Int, length: Int)
    fun mix(pcmData: ByteArray) = mix(pcmData, 0, pcmData.size)
    fun getVolumeController(): VolumeController
    fun setVolumeController(volumeController: VolumeController)

    fun open()
    fun pause()
}