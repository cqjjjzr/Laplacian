package charlie.laplacian.decoder

import charlie.laplacian.plugin.Plugin

interface Decoder {
    fun play()
    fun pause()
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

    fun getPlugin(): Plugin
}