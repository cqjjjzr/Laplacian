package charlie.laplacian.decoder.essential

import charlie.laplacian.I18n
import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.TrackStream
import io.humble.video.Demuxer
import io.humble.video.MediaAudio
import io.humble.video.MediaDescriptor
import io.humble.video.Rational
import io.humble.video.javaxsound.AudioFrame
import io.humble.video.javaxsound.MediaAudioConverterFactory
import java.util.*

class FFmpegDecoder(private val stream: TrackStream): Decoder {
    companion object{
        val streams: MutableMap<String, TrackStream> = HashMap()
    }

    private var serial: String = System.nanoTime().toString()
    private var demuxer: Demuxer = Demuxer.make()
    private var audioFrame: AudioFrame? = null
    private var durationMillis: Long = 0
    override fun play() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seek(positionMillis: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun positionMillis(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun durationMillis(): Long = durationMillis

    override fun volumeTo(percent: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun volume(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun calculateDuration(samples: MediaAudio) {
        durationMillis =
                samples.timeBase.multiply(Rational.make(1000.0)) // Second to Millisecond
                .multiply(Rational.make(samples.pts.toDouble()))
                .value.toLong()
    }

    private fun findAudioDecoder(): io.humble.video.Decoder {
        demuxer.numStreams.apply {
            for (i in 0..this) {
                demuxer.getStream(i).apply {
                    if (decoder != null && decoder.codecType == MediaDescriptor.Type.MEDIA_AUDIO)
                        return decoder
                }
            }
        }
        throw IllegalStateException(I18n.getString("decoder.exception.noAudioStream"))
    }

    init {
        streams[serial] = stream

        demuxer = Demuxer.make()
        demuxer.open("laplacian://$serial", null, false, true, null, null)

        findAudioDecoder().apply {
            open(null, null)

            audioFrame = AudioFrame.make(
                    MediaAudioConverterFactory.createConverter(
                            MediaAudioConverterFactory.DEFAULT_JAVA_AUDIO,
                            MediaAudio.make(
                                    frameSize, sampleRate, channels, channelLayout, sampleFormat
                            ).apply { calculateDuration(this) }
                    ).javaFormat
            )
        }
    }

    override fun close() {
        streams.remove(serial)
        demuxer.close()
        stream.close()
    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(stream: TrackStream): Decoder {
        return FFmpegDecoder(stream)
    }
}