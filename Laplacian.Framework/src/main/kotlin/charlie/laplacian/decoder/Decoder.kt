package charlie.laplacian.decoder

import charlie.laplacian.output.OutputSettings
import charlie.laplacian.stream.TrackStream

interface DecoderSession {
    fun read(buf: ByteArray?): ByteArray?
    fun seek(positionMillis: Long)
    fun positionMillis(): Long
    fun durationMillis(): Long

    fun close()
}

interface Decoder {
    fun getSession(outputSettings: OutputSettings, stream: TrackStream): DecoderSession

    fun getMetadata(): DecoderMetadata
}

interface DecoderMetadata {
    fun getName(): String
    fun getVersion(): String
    fun getVersionID(): Int
}