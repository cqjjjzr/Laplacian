package charlie.laplacian.decoder.essential.ffmpeg.internal;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ffmpeg.avutil55.Avutil55Library;

import java.nio.IntBuffer;

import static org.ffmpeg.avutil55.Avutil55Library.*;

public class FFmpegLogHelper implements Avutil55Library.av_log_set_callback_callback_callback {
    private Logger logger = LogManager.getLogger("decoder");
    private IntBuffer prefix = IntBuffer.allocate(1);

    private static FFmpegLogHelper INSTANCE = new FFmpegLogHelper();

    @Override
    public void apply(Pointer voidPtr1, int levelID, Pointer formatPtr, Pointer args) {
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

        Memory line = new Memory(128L);
        Avutil55Library.INSTANCE.av_log_format_line(voidPtr1, levelID, formatPtr, args, line.share(0), 128, prefix);
        String message = String.format(line.getString(0), args).trim();
        logger.log(level, message);
    }

    public static void initLogHelper() {
        Avutil55Library.INSTANCE.av_log_set_callback(INSTANCE);
    }
}
