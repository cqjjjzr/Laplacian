package charlie.laplacian.playctrl

import charlie.laplacian.config.Configuration
import charlie.laplacian.decoder.Decoder
import charlie.laplacian.source.SourceRegistry
import charlie.laplacian.track.Track
import charlie.laplacian.track.grouping.TrackGroupingMethod
import java.util.*

object PlayController {
    var repeatMode = RepeatMode.NO_REPEAT
    var playMode = PlayMode.ORDERED
    private val temp: LinkedList<Track> = LinkedList()
    private val tracks: LinkedList<Track> = LinkedList()
    private var currentTrackIndex = 0
    private var currentTrack: Track? = null
    private var decoder: Decoder? = null

    fun addTemp(track: Track) {
        temp.addFirst(track)
    }

    fun addTemp(tracks: TrackGroupingMethod) {
        temp.addAll(0, tracks.getTracks())
    }

    fun getTemps(): List<Track> = Collections.unmodifiableList(temp)

    fun removeTemp(index: Int) {
        temp.removeAt(index)
    }

    fun clearTemp() {
        temp.clear()
    }

    fun getTracks(): List<Track> = Collections.unmodifiableList(tracks.subList(currentTrackIndex + 1, tracks.size - 1))

    fun reload(trackIndex: Int, tracks: TrackGroupingMethod) {
        if (tracks.getTracks()[trackIndex] == currentTrack) {
            currentTrack = tracks.getTracks()[trackIndex]
            this.tracks.clear()
            this.tracks.addAll(tracks.getTracks())
            if (playMode == PlayMode.RANDOM) Collections.shuffle(this.tracks)
            currentTrackIndex = this.tracks.indexOf(currentTrack!!)
        } else {
            stop()
            this.tracks.clear()
            this.tracks.addAll(tracks.getTracks())
            if (playMode == PlayMode.RANDOM) Collections.shuffle(this.tracks)
            currentTrackIndex = this.tracks.indexOf(currentTrack!!)
            if (currentTrackIndex == -1) currentTrack = null
            start()
        }
    }

    fun start() {
        decoder = Configuration.getDecoder(SourceRegistry.getStream(tracks[currentTrackIndex].sourceInfo))
        decoder!!.play()
    }

    fun stop() {
        if (decoder != null) {
            decoder!!.close()
        }
        currentTrackIndex = -1
        currentTrack = null
    }
}

enum class RepeatMode {
    LIST_REPEAT, NO_REPEAT, SINGLE_REPEAT
}

enum class PlayMode {
    RANDOM, ORDERED
}