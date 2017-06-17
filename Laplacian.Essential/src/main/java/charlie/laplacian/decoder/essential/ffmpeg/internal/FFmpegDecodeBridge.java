package charlie.laplacian.decoder.essential.ffmpeg.internal;

import charlie.laplacian.decoder.DecoderException;
import charlie.laplacian.decoder.InvalidInputMediaException;
import charlie.laplacian.decoder.essential.FFmpegException;
import charlie.laplacian.source.essential.FileSource;
import charlie.laplacian.source.essential.FileTrackSourceInfo;
import charlie.laplacian.stream.SeekableTrackStream;
import charlie.laplacian.stream.SizeKnownTrackStream;
import charlie.laplacian.stream.TrackStream;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.ffmpeg.avformat.AVFormatContext;
import org.ffmpeg.avformat.AVFormatLibrary;
import org.ffmpeg.avformat.AVIOContext;
import org.ffmpeg.avformat.AVInputFormat;

import java.io.File;
import java.nio.charset.Charset;

import static org.ffmpeg.avformat.AVFormatLibrary.*;
import static org.ffmpeg.avutil.AVUtilLibrary.AVERROR_EOF;
import static org.ffmpeg.avutil.AVUtilLibrary.AVMediaType.AVMEDIA_TYPE_AUDIO;

@SuppressWarnings("unchecked")
public class FFmpegDecodeBridge {
    public static final int FFMPEG_IO_BUFFER_SIZE = 1024 * 512; // 512 KB
    public static final Pointer<Byte> EMPTY_CSTRING = (Pointer<Byte>) Pointer.pointerToString("", Pointer.StringType.C, Charset.defaultCharset());

    private Pointer<Byte> buffer;
    private AVFormatLibrary.avio_alloc_context_read_packet_callback readPacket;
    private AVFormatLibrary.avio_alloc_context_seek_callback seek;

    private Pointer<AVIOContext> ioContext;
    private Pointer<AVFormatContext> formatContext;
    private Pointer<AVInputFormat> inputFormat;

    private TrackStream stream;
    private int audioStreamIndex = -1;

    public static void init() {
        BridJ.register();
        AVFormatLibrary.av_register_all();

        //FFmpegLogHelper.initLogHelper();
    }

    public void open(TrackStream stream) throws DecoderException {
        this.stream = stream;
        openIOContext();
        initFormatContext();
        probeInputFormat();
        openFormatContext();
        findStreamInfo();
    }

    private void initFormatContext() {
        formatContext = AVFormatLibrary.avformat_alloc_context();
        AVFormatContext context = formatContext.get();
        context.flags(context.flags() | (AVFMT_FLAG_CUSTOM_IO | AVFMT_NOFILE));
        context.pb(ioContext);
        context.probesize(FFMPEG_IO_BUFFER_SIZE);
    }

    @SuppressWarnings("deprecation")
    private void openIOContext() throws FFmpegException {
        buffer = Pointer.allocateBytes(FFMPEG_IO_BUFFER_SIZE);
        readPacket = new AVIOReadPacketImpl();
        seek = new AVIOSeekImpl();

        ioContext = AVFormatLibrary.avio_alloc_context(buffer, FFMPEG_IO_BUFFER_SIZE,
                0, // Write disabled
                null,
                Pointer.getPointer(readPacket),
                null, // Write disabled
                Pointer.getPointer(seek));
        if (ioContext == null) throw new FFmpegException("nullptr returned from avio_alloc_context");

        ioContext.get().seekable(stream instanceof SeekableTrackStream ? 1 : 0);
    }

    private void probeInputFormat() throws FFmpegException {
        Pointer tempPtr = Pointer.pointerToPointer(inputFormat);
        int retval = AVFormatLibrary.av_probe_input_buffer(ioContext,
                tempPtr,
                Pointer.pointerToByte((byte) '\0'),
                null,
                0,
                FFMPEG_IO_BUFFER_SIZE);
        if (retval != 0)
            throw new FFmpegException("Failed to probe stream format! av_probe_input_buffer returned error code " + retval);
    }

    private void openFormatContext() throws FFmpegException {
        int retval = AVFormatLibrary.avformat_open_input(Pointer.pointerToPointer(formatContext),
                EMPTY_CSTRING,
                inputFormat,
                null);
        if (retval != 0)
            throw new FFmpegException("Failed to open input, avformat_open_input returned error code " + retval);
    }

    private void findStreamInfo() throws DecoderException {
        int retval = AVFormatLibrary.avformat_find_stream_info(formatContext, null);
        if (retval != 0)
            throw new FFmpegException("Failed to find stream info, avformat_find_stream_info returned error code " + retval);

        AVFormatContext context = formatContext.get();
        AVFormatLibrary.av_dump_format(formatContext, 0, EMPTY_CSTRING, 0);
        int nStreams = context.nb_streams();
        for (int i = 0; i < nStreams; i++) {
            if (context.streams().get(i).get().codecpar().get().codec_type() == AVMEDIA_TYPE_AUDIO) {
                audioStreamIndex = i;
                break;
            }
        }

        if (audioStreamIndex == -1)
            throw new InvalidInputMediaException("Audio stream not found!");
    }

    public void close() {
        // TODO Close
    }

    @SuppressWarnings("deprecation")
    private class AVIOReadPacketImpl extends AVFormatLibrary.avio_alloc_context_read_packet_callback {
        @Override
        public int apply(Pointer<?> opaque, Pointer<Byte> buf, int buf_size) {
            try {
                byte[] tempbuf = new byte[buf_size];
                int readLen = stream.read(tempbuf, 0, buf_size);
                buf.setBytes(tempbuf);
                return readLen == -1 ? AVERROR_EOF : readLen;
            } catch (Exception ex) {
                return -1;
            }
        }
    }

    private class AVIOSeekImpl extends AVFormatLibrary.avio_alloc_context_seek_callback {
        @Override
        public long apply(Pointer<?> opaque, long offset, int whence) {
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
        FFmpegDecodeBridge.init();
        new FFmpegDecodeBridge().open(new FileSource().streamFrom(new FileTrackSourceInfo(
                new File("E:\\iTunes\\iTunes Media\\Music\\Yonder Voice\\雪幻ティルナノーグ\\01 雪幻ティルナノーグ.m4a"))));
    }
}