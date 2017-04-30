package charlie.laplacian.output

import java.util.*
import kotlin.NoSuchElementException

object OutputMethodRegistry {
    private val mixers: MutableList<OutputMethod> = LinkedList()

    fun registerOutputMethod(outputMethod: OutputMethod) {
        mixers += outputMethod
    }

    fun unregisterOutputMethod(outputMethod: OutputMethod){
        mixers -= outputMethod
    }

    fun getMetadatas(): Array<OutputMethodMetadata> = Array(mixers.size, { mixers[it].getMetadata() })

    fun getOutputMethod(methodClassName: String): OutputMethod {
        return mixers.find { it.javaClass.name == methodClassName } ?: throw NoSuchElementException(methodClassName)
    }
}