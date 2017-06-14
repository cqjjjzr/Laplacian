package charlie.laplacian.decoder.essential.bridge;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface LogFormatterLibrary {
    LogFormatterLibrary INTERFACE = (LogFormatterLibrary) Native.loadLibrary("LogFormatter", LogFormatterLibrary.class);

    Pointer formatLog(String fmt, Pointer vl);
}
