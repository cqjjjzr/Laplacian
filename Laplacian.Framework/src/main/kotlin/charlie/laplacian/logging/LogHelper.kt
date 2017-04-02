package charlie.laplacian.logging

import charlie.laplacian.I18n
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.util.*

@Synchronized
fun Logger.logException(exception: Throwable, @Nls message: String, level: Level)  {
    log(level, message)
    StringTokenizer(
            String(
                    ByteArrayOutputStream().apply {
                        exception.printStackTrace(PrintWriter(this))
                    }
                            .toByteArray()), "\n").toList().forEach {
        log(level, (it as String).trim())
    }
}

fun Logger.logI18n(level: Level, @NonNls key: String) {
    log(level, I18n.getString(key))
}

fun Logger.infoI18n(@NonNls key: String) {
    info(I18n.getString(key))
}

fun Logger.warnI18n(@NonNls key: String) {
    warn(I18n.getString(key))
}

fun Logger.warnFormatI18n(@NonNls key: String, vararg parameters: Any) {
    warn(I18n.format(key, *parameters))
}