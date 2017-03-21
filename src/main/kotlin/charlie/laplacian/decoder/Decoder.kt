package charlie.laplacian.decoder

interface Decoder {
    fun play()
    fun pause()
    fun seek(positionMillis: Long)
    fun positionMillis(): Long
    fun durationMillis(): Long

    fun getVolumeController(): VolumeController

    fun close()
}