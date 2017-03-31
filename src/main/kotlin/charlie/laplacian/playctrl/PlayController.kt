package charlie.laplacian.playctrl

import charlie.laplacian.track.Track
import charlie.laplacian.track.grouping.TrackGroupingMethod

interface PlayController {
    fun load(focusTrack: Track, tracks: List<Track>)
    fun setRepeatMode(mode: RepeatMode)
    fun getRepeatMode(): RepeatMode

    fun addTemp(track: Track)
    fun addTemp(tracks: TrackGroupingMethod)

    fun getTemps(): List<Track>
    fun getTracks(): List<Track>
}

enum class RepeatMode {
    LIST_REPEAT, NO_REPEAT, SINGLE_REPEAT
}