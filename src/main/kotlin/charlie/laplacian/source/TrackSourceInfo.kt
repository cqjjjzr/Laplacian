package charlie.laplacian.source

import java.io.Serializable

interface TrackSourceInfo: Serializable {
    fun getSourceClass(): Class<out TrackSource>
}