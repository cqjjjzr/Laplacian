package charlie.laplacian.decoder

import charlie.laplacian.plugin.Plugin

interface DecoderMetadata {
    fun getName(): String
    fun getVersion(): String
    fun getVersionID(): Int

    fun getPlugin(): Plugin
}