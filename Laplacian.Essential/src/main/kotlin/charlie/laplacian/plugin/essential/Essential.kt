package charlie.laplacian.plugin.essential

import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.decoder.essential.FFmpegDecoderFactory
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import ro.fortsoft.pf4j.Plugin
import ro.fortsoft.pf4j.PluginWrapper

class Essential(wrapper: PluginWrapper?) : Plugin(wrapper) {
    companion object {
        val VERSION = 1
        val AUTHOR = "Charlie Jiang"
    }

    private val decoderFactory = FFmpegDecoderFactory()
    private val outputMethod = JavaSoundOutputMethod()

    override fun start() {
        DecoderRegistry.registerDecoderFactory(decoderFactory)
        OutputMethodRegistry.registerOutputMethod(outputMethod)
    }

    override fun stop() {
        DecoderRegistry.unregisterDecoderFactory(decoderFactory)
        OutputMethodRegistry.unregisterOutputMethod(outputMethod)
    }
}