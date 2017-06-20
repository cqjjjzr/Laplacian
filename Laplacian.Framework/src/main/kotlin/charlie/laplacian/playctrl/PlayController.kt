package charlie.laplacian.playctrl

import charlie.laplacian.config.Configuration
import charlie.laplacian.decoder.Decoder
import charlie.laplacian.source.SourceRegistry
import charlie.laplacian.source.TrackSourceInfo
import charlie.laplacian.track.Track
import charlie.laplacian.track.grouping.TrackGroupingMethod
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

object PlayController {
    var repeatMode = RepeatMode.NO_REPEAT
    var playMode = PlayMode.ORDERED
    private val temp: LinkedList<Track> = LinkedList()
    private val tracks: LinkedList<Track> = LinkedList()
    private var currentTrackIndex = 0
    private lateinit var currentTrack: Track
    private lateinit var decoder: Decoder

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
            currentTrackIndex = this.tracks.indexOf(currentTrack)
        } else {
            stop()
            this.tracks.clear()
            this.tracks.addAll(tracks.getTracks())
            if (playMode == PlayMode.RANDOM) Collections.shuffle(this.tracks)
            currentTrackIndex = this.tracks.indexOf(currentTrack)
            start()
        }
    }

    fun start() {
        decoder = Configuration.getDecoder(SourceRegistry.getStream(tracks[currentTrackIndex].sourceInfo))
        //decoder.play()
    }

    fun stop() {
        decoder.close()
        currentTrackIndex = -1
    }
}

enum class RepeatMode {
    LIST_REPEAT, NO_REPEAT, SINGLE_REPEAT
}

enum class PlayMode {
    RANDOM, ORDERED
}

class OutputHelper(sourceInfo: TrackSourceInfo) {
    private var decoder = Configuration.getDecoder(SourceRegistry.getStream(sourceInfo))
    private var outputSetting = Configuration.getOutputSettings()
    private var outputDevice = Configuration.getOutputDevice(outputSetting)
    private var outputLine = outputDevice.openLine()
    @Volatile
    private var paused = true
    @Volatile
    private var closed = false
    private val pauseLock = ReentrantLock()
    private val pauseCondition = pauseLock.newCondition()
    private var thread: Thread

    fun play() {
        paused = false
        outputLine.open()

        pauseLock.lock()
        pauseCondition.signalAll()
        pauseLock.unlock()
    }

    fun pause() {
        paused = true
        outputLine.pause()
    }

    fun close() {
        closed = true
        paused = true

        outputDevice.closeLine(outputLine)
    }

    init {
        thread = thread(
                start = false,
                isDaemon = true,
                name = "Laplacian-PlayThread-$sourceInfo") {
            var buf = decoder.read()
            while (buf != null) {
                outputLine.mix(buf)
                while (paused) {
                    pauseLock.lock()
                    pauseCondition.await()
                    pauseLock.unlock()
                }
                if (closed) return@thread
                buf = decoder.read()
            }
        }
    }
}