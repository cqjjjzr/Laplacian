package charlie.laplacian.decoder

interface Decoder {
    fun play()
    fun stop()
    fun pause()
    fun seek(positionMillis: Long)
    fun positionMillis(): Long
    fun durationMillis(): Long

    fun volumeTo(percent: Long)
    fun volume(): Long

    fun close()
}