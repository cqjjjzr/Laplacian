package charlie.laplacian.decoder.essential.ffmpeg.internal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.ffmpeg.avutil.AVUtilLibrary;

import static org.ffmpeg.avutil.AVUtilLibrary.*;

public class FFmpegLogHelper extends AVUtilLibrary.av_log_set_callback_callback_callback {
    private Logger logger = LogManager.getLogger("decoder");

    @Override
    public void apply(Pointer<?> voidPtr1, int levelID, Pointer<Byte> formatPtr, Pointer args) {
        Level level;
        switch(levelID) { // SEE FFmpeg libavutil/log.h
            case AV_LOG_QUIET:
            case AV_LOG_TRACE:
                level = Level.TRACE; break;
            case AV_LOG_PANIC:
            case AV_LOG_FATAL:
                level = Level.FATAL; break;
            case AV_LOG_ERROR:
                level = Level.ERROR; break;
            case AV_LOG_WARNING:
                level = Level.WARN; break;
            case AV_LOG_INFO:
                level = Level.INFO; break;
            case AV_LOG_VERBOSE:
            case AV_LOG_DEBUG:
                level = Level.DEBUG; break;
            default:
                level = Level.DEBUG;
        }
        //String message = String.format(formatPtr.getString(Pointer.StringType.C), args);
        String message = formatPtr.getString(Pointer.StringType.C);
        logger.log(level, message);
    }

    public static void initLogHelper() {
        FFmpegLogHelper helper = new FFmpegLogHelper();
        AVUtilLibrary.av_log_set_callback(Pointer.getPointer(helper));
        BridJ.protectFromGC(helper);
    }
}
