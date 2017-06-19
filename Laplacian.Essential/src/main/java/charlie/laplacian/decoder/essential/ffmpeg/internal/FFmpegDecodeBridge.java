package charlie.laplacian.decoder.essential.ffmpeg.internal;

import charlie.laplacian.decoder.DecoderException;
import charlie.laplacian.decoder.InvalidInputMediaException;
import charlie.laplacian.decoder.UnsupportedCodecException;
import charlie.laplacian.decoder.essential.FFmpegException;
import charlie.laplacian.output.OutputDevice;
import charlie.laplacian.output.OutputLine;
import charlie.laplacian.output.OutputMethod;
import charlie.laplacian.output.OutputSettings;
import charlie.laplacian.output.essential.JavaSoundOutputMethod;
import charlie.laplacian.source.essential.FileSource;
import charlie.laplacian.source.essential.FileTrackSourceInfo;
import charlie.laplacian.stream.SeekableTrackStream;
import charlie.laplacian.stream.SizeKnownTrackStream;
import charlie.laplacian.stream.TrackStream;
import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.ffmpeg.avcodec57.AVCodec;
import org.ffmpeg.avcodec57.AVCodecContext;
import org.ffmpeg.avcodec57.AVPacket;
import org.ffmpeg.avcodec57.Avcodec57Library;
import org.ffmpeg.avformat57.*;
import org.ffmpeg.avutil55.AVDictionary;
import org.ffmpeg.avutil55.AVFrame;
import org.ffmpeg.avutil55.AVRational;
import org.ffmpeg.avutil55.Avutil55Library;
import org.ffmpeg.swresample2.SwrContext;
import org.ffmpeg.swresample2.Swresample2Library;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

import static org.ffmpeg.avformat57.Avformat57Library.*;
import static org.ffmpeg.avutil55.Avutil55Library.AVERROR_EOF;
import static org.ffmpeg.avutil55.Avutil55Library.AVMediaType.AVMEDIA_TYPE_AUDIO;
import static org.ffmpeg.avutil55.Avutil55Library.AVSampleFormat.*;
import static org.ffmpeg.avutil55.Avutil55Library.AV_TIME_BASE;

public class FFmpegDecodeBridge {
    public static final int FFMPEG_IO_BUFFER_SIZE = 1024 * 512; // 512 KB
    public static final int MAX_AUDIO_FRAME_SIZE = 192000;

    private Pointer buffer;
    private Avformat57Library.avio_alloc_context_read_packet_callback readPacket;
    private Avformat57Library.avio_alloc_context_seek_callback seek;
    private AVIOContext.ByReference ioContext;

    private Pointer audioBuffer = null;
    private AVFormatContext.ByReference formatContext;
    private AVInputFormat inputFormat;
    private AVCodecContext codecContext;
    private SwrContext swrContext;

    private TrackStream stream;
    private OutputSettings outputSettings;
    private int audioStreamIndex = -1;
    private int sampleFormat;

    private long position = 0;

    public FFmpegDecodeBridge(TrackStream stream, OutputSettings outputSettings) throws DecoderException {
        if (outputSettings.getBitDepth() != 32 && outputSettings.getBitDepth() != 16 && outputSettings.getBitDepth() != 64)
            throw new IllegalArgumentException("bit depth must be 32, 64 or 16");

        this.stream = stream;
        this.outputSettings = outputSettings;

        open();
    }

    public static void init() {
        Avformat57Library.INSTANCE.av_register_all();

        FFmpegLogHelper.initLogHelper();
    }

    private void open() throws DecoderException {
        openIOContext();
        initFormatContext();
        probeInputFormat();
        openFormatContext();
        findStreamInfoAndOpenDecoder();
        prepareResample();
        prepareDecode();
    }

    private void initFormatContext() {
        formatContext = Avformat57Library.INSTANCE.avformat_alloc_context();
        formatContext.flags |= (AVFMT_FLAG_CUSTOM_IO | AVFMT_NOFILE);
        formatContext.pb = ioContext;
        formatContext.probesize = FFMPEG_IO_BUFFER_SIZE;
    }

    private void openIOContext() throws FFmpegException {
        buffer = Avutil55Library.INSTANCE.av_malloc(new NativeSize(FFMPEG_IO_BUFFER_SIZE));
        readPacket = new AVIOReadPacketImpl();
        seek = new AVIOSeekImpl();

        ioContext = Avformat57Library.INSTANCE.avio_alloc_context(buffer, FFMPEG_IO_BUFFER_SIZE,
                0, // Write disabled
                null,
                readPacket,
                null, // Write disabled
                seek);
        if (ioContext == null) {
            close();
            throw new FFmpegException("nullptr returned from avio_alloc_context");
        }

        ioContext.seekable = stream instanceof SeekableTrackStream ? 1 : 0;
    }

    private void probeInputFormat() throws FFmpegException {
        PointerByReference ptr = new PointerByReference();
        int retval = Avformat57Library.INSTANCE.av_probe_input_buffer(ioContext,
                ptr,
                "",
                Pointer.NULL,
                0,
                FFMPEG_IO_BUFFER_SIZE);
        if (retval != 0)
            throw new FFmpegException("Failed to probe stream format! av_probe_input_buffer returned error code " + retval);
        inputFormat = new AVInputFormat(ptr.getValue()); // FUCK OUTPUT ARGUMENT!
        inputFormat.autoRead();
    }

    private void openFormatContext() throws FFmpegException {
        PointerByReference ptr = new PointerByReference(formatContext.getPointer());
        AVFormatContext.ByReference[] tempArr = new AVFormatContext.ByReference[] { formatContext };
        int retval = Avformat57Library.INSTANCE.avformat_open_input(tempArr,
                "",
                inputFormat,
                null);
        if (retval != 0) {
            close();
            throw new FFmpegException("Failed to open input, avformat_open_input returned error code " + retval);
        }
    }

    private void findStreamInfoAndOpenDecoder() throws DecoderException {
        int retval = Avformat57Library.INSTANCE.avformat_find_stream_info(formatContext, (AVDictionary.ByReference[]) null);
        if (retval != 0)
            throw new FFmpegException("Failed to find stream info, avformat_find_stream_info returned error code " + retval);

        for (int i = 0; i < formatContext.nb_streams; i++) {
            AVStream stream = new AVStream(formatContext.streams.getPointer(i));
            stream.read();
            if (stream.codecpar.codec_type == AVMEDIA_TYPE_AUDIO) {
                audioStreamIndex = i;
                openDecoder(stream);
                break;
            }
        }

        if (audioStreamIndex == -1) {
            close();
            throw new InvalidInputMediaException("Audio stream not found!");
        }
    }

    private void openDecoder(AVStream stream) throws DecoderException {
        AVCodec codec = Avcodec57Library.INSTANCE.avcodec_find_decoder(stream.codecpar.codec_id);
        if (codec == null) {
            close();
            throw new UnsupportedCodecException("Codec ID " + stream.codecpar.codec_id + " not supported!");
        }
        codecContext = Avcodec57Library.INSTANCE.avcodec_alloc_context3(codec);
        codecContext.setAutoSynch(false);

        int retval;
        retval = Avcodec57Library.INSTANCE.avcodec_parameters_to_context(codecContext, stream.codecpar);
        if (retval < 0) {
            close();
            throw new FFmpegException("Unknown error, avcodec_parameters_to_context returned " + retval);
        }

        retval = Avcodec57Library.INSTANCE.avcodec_open2(codecContext, codec, (AVDictionary.ByReference[]) null);
        if (retval < 0) {
            close();
            throw new FFmpegException("Unknown error, avcodec_open2 returned " + retval);
        }
    }

    private void prepareResample() {
        swrContext = Swresample2Library.INSTANCE.swr_alloc();
        codecContext.read();

        sampleFormat = outputSettings.getBitDepth() == 16 ? AV_SAMPLE_FMT_S16 :
                (outputSettings.getBitDepth() == 32 ? AV_SAMPLE_FMT_S32 : AV_SAMPLE_FMT_S64);
        swrContext = Swresample2Library.INSTANCE.swr_alloc_set_opts(
                swrContext,
                Avutil55Library.INSTANCE.av_get_default_channel_layout(outputSettings.getNumChannels()),
                sampleFormat,
                (int) outputSettings.getSampleRateHz(),
                codecContext.channel_layout,
                codecContext.sample_fmt,
                codecContext.sample_rate,
                0,
                null);
        Swresample2Library.INSTANCE.swr_init(swrContext);
    }

    private void prepareDecode() {
        audioBuffer = Avutil55Library.INSTANCE.av_malloc(new NativeSize(MAX_AUDIO_FRAME_SIZE * 3 / 2 * Native.LONG_SIZE));
    }

    public void close() {
        if (formatContext != null) {
            PointerByReference ptr = new PointerByReference(formatContext.getPointer());
            Avformat57Library.INSTANCE.avformat_close_input(ptr);
        }
        formatContext = null;
        buffer = null;
    }

    private AVPacket packet = new AVPacket();
    @Nullable
    public byte[] tryRead() throws FFmpegException {
        if (formatContext == null) throw new IllegalStateException("Decoder is already closed");

        int retval;
        while (true) {
            retval = Avformat57Library.INSTANCE.av_read_frame(formatContext, packet);
            if (retval < 0) return null;
            packet.read();
            if (packet.stream_index != audioStreamIndex) {
                Avcodec57Library.INSTANCE.av_packet_unref(packet);
            } else break;
        }

        retval = Avcodec57Library.INSTANCE.avcodec_send_packet(codecContext, packet.getPointer());
        if (retval < 0 && retval != -11 && retval != AVERROR_EOF) {
            throw new FFmpegException("Decode packet failed! avcodec_send_packet returned " + retval);
        }

        AVFrame frame = Avutil55Library.INSTANCE.av_frame_alloc();
        retval = Avcodec57Library.INSTANCE.avcodec_receive_frame(codecContext, frame);
        if (retval < 0 && retval != AVERROR_EOF) {
            throw new FFmpegException("Decode packet failed! avcodec_receive_frame returned " + retval);
        }
        frame.read();

        updatePosition(frame);

        fixFrameArgs(frame);

        long destNumSamplesPerChannel =
                Avutil55Library.INSTANCE.av_rescale_rnd(
                        Swresample2Library.INSTANCE.swr_get_delay(swrContext, frame.sample_rate) + frame.nb_samples,
                        frame.sample_rate, frame.sample_rate, 0);
        PointerByReference tempPtr = new PointerByReference(audioBuffer);
        int numSamples = Swresample2Library.INSTANCE.swr_convert(swrContext, tempPtr,
                (int) destNumSamplesPerChannel, frame.data, frame.nb_samples);
        int dataSize = frame.channels * numSamples * Avutil55Library.INSTANCE.av_get_bytes_per_sample(sampleFormat);
        Avutil55Library.INSTANCE.av_frame_free(new PointerByReference(frame.getPointer()));

        audioBuffer = tempPtr.getValue();
        return Arrays.copyOf(audioBuffer.getByteArray(0, dataSize), dataSize);
    }

    private void updatePosition(AVFrame frame) {
        AVRational.ByValue time = new AVRational.ByValue();
        time.den = codecContext.time_base.den;
        time.num = codecContext.time_base.num;
        position = (long) (frame.pts * (double) codecContext.time_base.num * 1000 / codecContext.time_base.den);
    }

    private void fixFrameArgs(AVFrame frame) {
        if (frame.channels > 0 && frame.channel_layout == 0)
            frame.channel_layout = Avutil55Library.INSTANCE.av_get_default_channel_layout(frame.channels);
        else if (frame.channels == 0 && frame.channel_layout > 0) {
            frame.channels = Avutil55Library.INSTANCE.av_get_channel_layout_nb_channels(frame.channel_layout);
        }
    }

    public void seek(long positionMillis) throws DecoderException {
        if (formatContext == null) throw new IllegalStateException("Decoder is already closed");

        int retval = Avformat57Library.INSTANCE.av_seek_frame(formatContext, -1,
                AV_TIME_BASE * positionMillis / 1000, AVSEEK_FLAG_BACKWARD);
        if (retval < 0)
            throw new FFmpegException("Failed to seek! av_seek_frame returned error code " + retval);
        this.position = positionMillis;
    }

    public long getDurationMillis() {
        if (formatContext == null) throw new IllegalStateException("Decoder is already closed");
        return formatContext.duration / 1000;
    }

    public long getPositionMillis() {
        if (formatContext == null) throw new IllegalStateException("Decoder is already closed");
        return position;
    }

    private class AVIOReadPacketImpl implements Avformat57Library.avio_alloc_context_read_packet_callback {
        @Override
        public int apply(Pointer opaque, Pointer buf, int buf_size) {
            try {
                byte[] tempbuf = new byte[buf_size];
                int readLen = stream.read(tempbuf, 0, buf_size);
                if (readLen == -1) return AVERROR_EOF;
                buf.write(0, tempbuf, 0, readLen);
                return readLen;
            } catch (Exception ex) {
                return -1;
            }
        }
    }

    private class AVIOSeekImpl implements Avformat57Library.avio_alloc_context_seek_callback {
        @Override
        public long apply(Pointer opaque, long offset, int whence) {
            boolean force = (whence & AVSEEK_FORCE) != 0;
            whence &= ~AVSEEK_FORCE;

            try {
                switch (whence) {
                    case 0: // SEEK_SET
                    case 1: // SEEK_CUR
                    case 2: // SEEK_END
                        int absPos = (int) offset + (whence == 1 ? stream.position() : 0);
                        if (whence == 2)
                            if (stream instanceof SizeKnownTrackStream)
                                absPos = (int) (((SizeKnownTrackStream) stream).size() - offset);
                            else
                                return -1;

                        if (stream instanceof SeekableTrackStream) {
                            ((SeekableTrackStream) stream).seek(absPos);
                        } else {
                            if (force) stream.forceSeek(absPos);
                            else return -1;
                        }
                        return stream.position();

                    case AVSEEK_SIZE:
                        if (stream instanceof SizeKnownTrackStream)
                            return ((SizeKnownTrackStream) stream).size();
                        else
                            return -1;
                    default:
                        return -1;
                }
            } catch (Exception ex) {
                return -1;
            }
        }
    }

    public static void main(String[] args) throws DecoderException {
        OutputSettings settings = new OutputSettings(44100,16,2);
        FFmpegDecodeBridge.init();
        FFmpegDecodeBridge bridge = new FFmpegDecodeBridge(new FileSource().streamFrom(new FileTrackSourceInfo(
                new File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a"))),
                settings);
        OutputMethod method = new JavaSoundOutputMethod();
        OutputDevice device = method.openDevice(settings);
        OutputLine line = device.openLine();
        line.open();
        bridge.seek(5000);
        byte[] buf;
        while ((buf = bridge.tryRead()) != null)
            line.mix(buf, 0, buf.length);
        device.closeLine(line);
        bridge.close();
    }
}