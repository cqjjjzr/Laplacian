package charlie.laplacian.decoder

interface VolumeController {
    fun max(): Float
    fun min(): Float
    fun current(): Float
    fun set(value: Float)
}