package io.devpl.fxui.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    //-----------------------------------------------------------------------

    /**
     * <p>Gets the stack trace from a Throwable as a String.</p>
     *
     * <p>The result of this method vary by JDK version as this method
     * uses {@link Throwable#printStackTrace(PrintWriter)}.
     * On JDK1.3 and earlier, the cause exception will not be shown
     * unless the specified throwable alters printStackTrace.</p>
     * @param throwable the {@code Throwable} to be examined
     * @return the stack trace as generated by the exception's
     * {@code printStackTrace(PrintWriter)} method
     */
    public static String getStackTrace(final Throwable throwable) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw, true)) {
            throwable.printStackTrace(pw);
            return sw.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
