package charlie.laplacian.output

interface VolumeController {
    fun max(): Float
    fun min(): Float
    fun current(): Float
    fun set(value: Float)
}