package charlie.laplacian.plugin.essential

import charlie.laplacian.decoder.essential.DecoderRegistry
import charlie.laplacian.decoder.essential.FFmpegDecoderFactory
import charlie.laplacian.plugin.Plugin
import charlie.laplacian.plugin.PluginMetadata

class Essential: Plugin {
    private val ffmpegDecoderFactory = FFmpegDecoderFactory()

    override fun getMetadata(): PluginMetadata {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun init() {
        DecoderRegistry.registerDecoderFactory(ffmpegDecoderFactory)
    }

    override fun destroy() {
        DecoderRegistry.unregisterDecoderFactory(ffmpegDecoderFactory)
    }
}