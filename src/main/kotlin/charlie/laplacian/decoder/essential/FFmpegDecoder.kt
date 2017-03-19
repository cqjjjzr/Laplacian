package charlie.laplacian.decoder.essential

import charlie.laplacian.decoder.Decoder
import charlie.laplacian.decoder.DecoderFactory
import charlie.laplacian.decoder.TrackStream

/*class FFmpegDecoder(private val stream: TrackStream): Decoder, Runnable {
    companion object{
        val streams: MutableMap<String, TrackStream> = HashMap()
    }

    private var serial: String = System.nanoTime().toString()
    private var demuxer: Demuxer = Demuxer.make()

    private var audioStreamIndex: Int = -1
    private var audioStream: DemuxerStream? = null
    private var decoder: io.humble.video.Decoder? = null

    private var rawAudio: ByteBuffer? = null
    private var samples: MediaAudio? = null
    private var audioFrame: AudioFrame? = null
    private var durationMillis: Long = 0

    private var converter: MediaAudioConverter? = null

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
        audioStream!!
                .timeBase
                    .multiply(Rational.make(1000.0)) // Second to Millisecond
                    .multiply(Rational.make(positionMillis.toDouble()))
                .apply {
                    demuxer.seek(audioStreamIndex,
                            0L,
                            this.value.toLong(),
                            this.value.toLong(),
                            Demuxer.SeekFlag.SEEK_BACKWARD.swigValue())
                }
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
                    if (decoder != null && decoder.codecType == MediaDescriptor.Type.MEDIA_AUDIO) {
                        audioStreamIndex = i
                        audioStream = this
                        return decoder
                    }
                }
            }
        }
        throw IllegalStateException(I18n.getString("decoder.exception.noAudioStream"))
    }

    init {
        streams[serial] = stream

        demuxer = Demuxer.make()
        demuxer.open("laplacian://$serial", null, false, true, null, null)

        decoder = findAudioDecoder()

        decoder!!.apply {
            open(null, null)
            samples = MediaAudio.make(
                    frameSize, sampleRate, channels, channelLayout, sampleFormat
            )

            converter = MediaAudioConverterFactory.createConverter(
                            MediaAudioConverterFactory.DEFAULT_JAVA_AUDIO,
                            samples)

            audioFrame = AudioFrame.make(converter!!.javaFormat)
        }

        // calculateDuration(samples!!)
        durationMillis = demuxer.duration.let {
            if (it == NO_PTS) -1
            else it / 1000 // Microsecond to millisecond
        }
    }

    override fun close() {
        streams.remove(serial)
        demuxer.close()
        stream.close()
    }

    override fun run() {
        MediaPacket.make().apply {
            while (demuxer.read(this) >= 0) {
                if (this.streamIndex == audioStreamIndex) {
                    var offset = 0
                    var readLength = 0
                    do {
                        readLength += decoder!!.decode(samples, this, offset)
                        if (samples!!.isComplete) {
                            rawAudio = converter!!.toJavaAudio(rawAudio, samples)
                            audioFrame!!.play(rawAudio)
                        }
                        offset += readLength
                    } while (offset < size)
                }
            }
        }
    }
}*/

class FFmpegDecoder(private val stream: TrackStream): Decoder {
    companion object {
        @JvmStatic
        val streams: MutableMap<String, TrackStream> = HashMap()
        @JvmStatic
        private external fun globalInit()
    }

    private val URL_PREFIX = "laplacian://"
    private val serial: String = System.nanoTime().toString()
    @Volatile
    private var paused: Boolean = true
    @Volatile
    private var pointerAVCodecContext: Long = 0
    @Volatile
    private var pointerAVFormatContext: Long = 0
    @Volatile
    private var pointerPacketQueue: Long = 0
    @Volatile
    private var volume: Int = 0


    @Volatile
    private var audioStreamIndex: Int = -1

    override external fun play()

    override external fun stop()

    override external fun pause()

    override external fun seek(positionMillis: Long)

    override external fun positionMillis(): Long

    override external fun durationMillis(): Long

    override external fun volumeTo(percent: Long)

    override external fun volume(): Long

    override external fun close()

    private external fun playThread()

    private external fun initNativeLib(stream: TrackStream, url: String)

    init {
        streams[serial] = stream
        initNativeLib(stream, URL_PREFIX + serial)
    }
}

class FFmpegDecoderFactory: DecoderFactory {
    override fun getDecoder(stream: TrackStream): Decoder {
        return FFmpegDecoder(stream)
    }
}