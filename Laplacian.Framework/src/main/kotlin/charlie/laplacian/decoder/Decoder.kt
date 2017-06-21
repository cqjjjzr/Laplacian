package charlie.laplacian.decoder

interface Decoder {
    fun read(buf: ByteArray?): ByteArray?
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