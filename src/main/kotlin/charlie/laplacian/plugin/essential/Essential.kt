package charlie.laplacian.plugin.essential

import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.decoder.essential.FFmpegDecoder
import charlie.laplacian.decoder.essential.FFmpegDecoderFactory

class Essential {
    private val ffmpegDecoderFactory = FFmpegDecoderFactory()

    fun init() {
        FFmpegDecoder.init()
        DecoderRegistry.registerDecoderFactory(ffmpegDecoderFactory)
    }

    fun destroy() {
        DecoderRegistry.unregisterDecoderFactory(ffmpegDecoderFactory)
    }

    /*class EssentialMetadata: PluginMetadata {
        override fun getName(): String = "Essential"

        override fun getDescriptor(): String = I18n.getString("plugin.essential.descriptor")

        override fun getVersion(): String = "rv1"

        override fun getVersionID(): Int = 1

        override fun getAuthor(): String = "Charlie Jiang"

        override fun getIcon(): Image? {
            // TODO ICON
            return null
        }
    }*/
}