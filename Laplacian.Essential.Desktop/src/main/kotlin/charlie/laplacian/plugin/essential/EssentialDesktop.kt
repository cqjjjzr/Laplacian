package charlie.laplacian.plugin.essential

//import charlie.laplacian.decoder.essential.FFmpegDecoderFactory
import charlie.laplacian.decoder.DecoderRegistry
import charlie.laplacian.decoder.essential.FFmpegDecoder
import charlie.laplacian.output.OutputMethodRegistry
import charlie.laplacian.output.essential.JavaSoundOutputMethod
import charlie.laplacian.source.SourceRegistry
import charlie.laplacian.source.essential.FileSource
import ro.fortsoft.pf4j.Plugin
import ro.fortsoft.pf4j.PluginWrapper

class EssentialDesktop(wrapper: PluginWrapper?) : Plugin(wrapper) {
    companion object {
        val VERSION = 1
        val AUTHOR = "Charlie Jiang"
    }

    private val decoder = FFmpegDecoder()
    private val outputMethod = JavaSoundOutputMethod()
    private val fileSource = FileSource()

    override fun start() {
        DecoderRegistry.registerDecoderFactory(decoder)
        FFmpegDecoder.init()
        OutputMethodRegistry.registerOutputMethod(outputMethod)
        SourceRegistry.registerSource(fileSource)
    }

    override fun stop() {
        DecoderRegistry.unregisterDecoderFactory(decoder)
        OutputMethodRegistry.unregisterOutputMethod(outputMethod)
        SourceRegistry.unregisterSource(fileSource)
    }
}