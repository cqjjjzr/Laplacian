package charlie.laplacian.decoder

interface Decoder {
    fun read(): ByteArray
    fun hasNext(): Boolean
    fun seek(positionMillis: Long)
    fun positionMillis(): Long
    fun durationMillis(): Long

    fun close()

    fun getMetadata(): DecoderMetadata
}

interface DecoderMetadata {
    fun getName(): String
    fun getVersion(): String
    fun getVersionID(): Int
}