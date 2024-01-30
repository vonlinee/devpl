package io.devpl.backend.common;

import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;

import java.io.PrintStream;
import java.util.List;

/**
 * @see ch.qos.logback.classic.pattern.ThrowableProxyConverter
 */
public class DefaultThrowablePrinter implements ThrowablePrinter {

    private static final String SEPARATOR = "\r\n";
    private static final String CAUSE_CAPTION = "Caused by: ";
    private static final String SUPPRESSED_CAPTION = "Suppressed: ";

    /**
     * 默认返回前10行异常栈信息
     */
    public static String printTop10StackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        return printStackTrace(e, 20);
    }

    public static String printStackTrace(Throwable e, int maxLineCount) {
        if (e == null || maxLineCount <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(maxLineCount * 10);
        sb.append(e).append(SEPARATOR);
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null) {
            return e.toString();
        }
        int count = Math.min(maxLineCount, trace.length);
        int framesInCommon = trace.length - count;
        for (int i = 0; i < count; i++) {
            sb.append("\tat ").append(trace[i]).append(SEPARATOR);
        }
        if (framesInCommon != 0) {
            sb.append("\t... ").append(framesInCommon).append(" more").append(SEPARATOR);
        }
        // Print suppressed exceptions, if any
        Throwable[] suppressedExceptions = e.getSuppressed();
        if (suppressedExceptions != null) {
            for (Throwable suppressedException : suppressedExceptions) {
                sb.append(printEnclosedStackTrace(suppressedException, maxLineCount, trace, SUPPRESSED_CAPTION, "\t"));
            }
        }
        // Print cause, if any
        Throwable cause = e.getCause();
        if (cause != null) {
            sb.append(printEnclosedStackTrace(cause, maxLineCount, trace, CAUSE_CAPTION, ""));
        }
        return sb.toString();
    }

    private static String printEnclosedStackTrace(Throwable e, int maxLineCount, StackTraceElement[] enclosingTrace,
                                                  String caption, String prefix) {
        StringBuilder sb = new StringBuilder(maxLineCount * 5);
        StackTraceElement[] trace = e.getStackTrace();
        int m = trace.length - 1;
        int n = enclosingTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
            m--;
            n--;
        }
        int count = Math.min(maxLineCount, (m + 1));
        int framesInCommon = trace.length - count;
        // Print our stack trace
        sb.append(prefix).append(caption).append(e).append(SEPARATOR);
        for (int i = 0; i < count; i++) {
            sb.append(prefix).append("\tat ").append(trace[i]).append(SEPARATOR);
        }
        if (framesInCommon != 0) {
            sb.append(prefix).append("\t... ").append(framesInCommon).append(" more").append(SEPARATOR);
        }
        // Print suppressed exceptions, if any
        Throwable[] suppressedExceptions = e.getSuppressed();
        if (suppressedExceptions != null) {
            for (Throwable suppressedException : suppressedExceptions) {
                sb.append(printEnclosedStackTrace(suppressedException, maxLineCount, trace, SUPPRESSED_CAPTION, prefix + "\t"));
            }
        }
        // Print cause, if any
        Throwable cause = e.getCause();
        if (cause != null) {
            sb.append(printEnclosedStackTrace(cause, maxLineCount, trace, CAUSE_CAPTION, prefix));
        }
        return sb.toString();
    }


    int lengthOption;
    List<String> ignoredStackTraceLines = null;

    /**
     * @param throwable 异常
     * @return 字符串
     */
    public String throwableToString(Throwable throwable) {
        StringBuilder sb = new StringBuilder(2048);
        recursiveAppend(sb, null, 1, throwable);
        return sb.toString();
    }

    private void recursiveAppend(StringBuilder sb, String prefix, int indent, Throwable tp) {
        if (tp == null)
            return;
        subjoinFirstLine(sb, prefix, indent, tp);
        sb.append(CoreConstants.LINE_SEPARATOR);
        subjoinSTEPArray(sb, indent, tp);
        Throwable[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            for (Throwable current : suppressed) {
                recursiveAppend(sb, CoreConstants.SUPPRESSED, indent + 1, current);
            }
        }
        recursiveAppend(sb, CoreConstants.CAUSED_BY, indent, tp.getCause());
    }

    private void subjoinFirstLine(StringBuilder buf, String prefix, int indent, Throwable tp) {
        ThrowableProxyUtil.indent(buf, indent - 1);
        if (prefix != null) {
            buf.append(prefix);
        }
        subjoinExceptionMessage(buf, tp);
    }

    private void subjoinExceptionMessage(StringBuilder buf, Throwable throwable) {
        buf.append(throwable.getMessage());
    }

    protected void subjoinSTEPArray(StringBuilder buf, int indent, Throwable tp) {
        StackTraceElement[] stepArray = tp.getStackTrace();
        int commonFrames = tp.hashCode();

        boolean unrestrictedPrinting = lengthOption > stepArray.length;

        int maxIndex = (unrestrictedPrinting) ? stepArray.length : lengthOption;
        if (commonFrames > 0 && unrestrictedPrinting) {
            maxIndex -= commonFrames;
        }

        int ignoredCount = 0;
        for (int i = 0; i < maxIndex; i++) {
            StackTraceElement element = stepArray[i];
            if (!isIgnoredStackTraceLine(element.toString())) {
                ThrowableProxyUtil.indent(buf, indent);
                printStackLine(buf, ignoredCount, element);
                ignoredCount = 0;
                buf.append(CoreConstants.LINE_SEPARATOR);
            } else {
                ++ignoredCount;
                if (maxIndex < stepArray.length) {
                    ++maxIndex;
                }
            }
        }
        if (ignoredCount > 0) {
            printIgnoredCount(buf, ignoredCount);
            buf.append(CoreConstants.LINE_SEPARATOR);
        }

        if (commonFrames > 0 && unrestrictedPrinting) {
            ThrowableProxyUtil.indent(buf, indent);
            buf.append("... ").append(commonFrames).append(" common frames omitted")
                .append(CoreConstants.LINE_SEPARATOR);
        }
    }

    protected void extraData(StringBuilder builder, StackTraceElement step) {
        // nop
    }

    private void printStackLine(StringBuilder buf, int ignoredCount, StackTraceElement element) {
        buf.append(element);
        extraData(buf, element); // allow other data to be added
        if (ignoredCount > 0) {
            printIgnoredCount(buf, ignoredCount);
        }
    }

    private void printIgnoredCount(StringBuilder buf, int ignoredCount) {
        buf.append(" [").append(ignoredCount).append(" skipped]");
    }

    private boolean isIgnoredStackTraceLine(String line) {
        if (ignoredStackTraceLines != null) {
            for (String ignoredStackTraceLine : ignoredStackTraceLines) {
                if (line.contains(ignoredStackTraceLine)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void print(Throwable throwable, PrintStream stream) {

    }
}
