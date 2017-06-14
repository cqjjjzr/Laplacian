package charlie.laplacian.decoder.essential.bridge;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface AVUtilLibrary {
    interface AVFormatLogCallback extends Callback {
        void invoke(Pointer ptr, int level, String fmt, Pointer fml);
    }

    AVUtilLibrary INSTANCE = (AVUtilLibrary) Native.loadLibrary("avutil-55", AVUtilLibrary.class);

    void av_log_set_callback(AVFormatLogCallback callback);

    Pointer av_malloc(long size);
}
