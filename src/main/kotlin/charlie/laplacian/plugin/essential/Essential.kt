package charlie.laplacian.plugin.essential

import charlie.laplacian.I18n
import charlie.laplacian.decoder.essential.DecoderRegistry
import charlie.laplacian.decoder.essential.FFmpegDecoder
import charlie.laplacian.decoder.essential.FFmpegDecoderFactory
import charlie.laplacian.plugin.Plugin
import charlie.laplacian.plugin.PluginMetadata

class Essential: Plugin {
    private val ffmpegDecoderFactory = FFmpegDecoderFactory()

    override fun getMetadata(): PluginMetadata {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun globalInit() {
        FFmpegDecoder.init()
    }

    override fun init() {
        DecoderRegistry.registerDecoderFactory(ffmpegDecoderFactory)
    }

    override fun destroy() {
        DecoderRegistry.unregisterDecoderFactory(ffmpegDecoderFactory)
    }

    class EssentialMetadata: PluginMetadata {
        override fun getName(): String = "Essential"

        override fun getDescriptor(): String = I18n.getString("plugin.essential.descriptor")

        override fun getVersion(): String = "rv1"

        override fun getVersionID(): Int = 1

        override fun getAuthor(): String = "Charlie Jiang"
    }
}