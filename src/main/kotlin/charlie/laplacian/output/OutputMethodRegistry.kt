package charlie.laplacian.output

import java.util.*

object OutputMethodRegistry {
    private val mixers: MutableList<OutputMethod> = LinkedList()

    fun registerMixer(outputMethod: OutputMethod) {
        mixers += outputMethod
    }

    fun unregisterMixer(outputMethod: OutputMethod){
        mixers -= outputMethod
    }

    fun getMetadatas(): Array<OutputMethodMetadata> = Array(mixers.size, { mixers[it].getMetadata() })

    fun getOutputMethod(methodClassName: String): OutputMethod {
        return mixers.filter { it.javaClass.name == methodClassName }.first()
    }
}