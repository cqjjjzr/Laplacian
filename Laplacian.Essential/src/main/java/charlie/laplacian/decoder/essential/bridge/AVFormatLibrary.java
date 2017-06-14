package charlie.laplacian.decoder.essential.bridge;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;

public interface AVFormatLibrary {
    interface AVIOReadPacketCallback extends Callback {
        int invoke(Pointer userData, Pointer buf, int bufSize);
    }

    interface AVIOWritePacketCallback extends Callback {
        int invoke(Pointer userData, Pointer buf, int bufSize);
    }

    interface AVIOSeekPacketCallback extends Callback {
        long invoke(Pointer userData, long offset, int whence);
    }

    class AVIOContext extends Structure {
        public static class ByReference extends AVIOContext implements Structure.ByReference {}
        public static class ByValue extends AVIOContext implements Structure.ByValue {}

        // TODO AAAAAAAAAAAAAAAAAAHHHHHHHHHHFUCK Add AVIOContext struct here!
        @Override
        protected List<String> getFieldOrder() {
            return null;
        }
    }

    class AVInputFormat extends Structure {
        public static class ByReference extends AVInputFormat implements Structure.ByReference {}
        public static class ByValue extends AVInputFormat implements Structure.ByValue {}

        // TODO AAAAAAAAAAAAAAAAAAHHHHHHHHHHFUCK Add AVIOContext struct here!
        @Override
        protected List<String> getFieldOrder() {
            return null;
        }
    }

    AVFormatLibrary INSTANCE = (AVFormatLibrary) Native.loadLibrary("avformat-57", AVFormatLibrary.class);

    void av_register_all();

    Pointer avformat_alloc_context();
    AVIOContext.ByReference avio_alloc_context(Pointer buffer,
                               int bufferSize,
                               int writeFlag,
                               Pointer userData,
                               AVIOReadPacketCallback readPacket,
                               AVIOWritePacketCallback writePacket,
                               AVIOSeekPacketCallback seekPacket);
    int av_probe_input_buffer(AVIOContext.ByReference avIOContext,
                                  Pointer outAVFormatContext,
                                  String url,
                                  Pointer avLogContext,
                                  int offset,
                                  int maxProbeSize);
    int avformat_open_input(Pointer outAVFormatContext,
                            String url,
                            AVInputFormat.ByReference avInputFormat,
                            Pointer dictArgs);
}
