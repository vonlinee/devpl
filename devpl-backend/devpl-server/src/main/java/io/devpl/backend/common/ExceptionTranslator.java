package io.devpl.backend.common;

import java.io.PrintStream;
import java.util.function.Predicate;

public class ExceptionTranslator {

    private final Throwable throwable;
    private Predicate<StackTraceElement> filter;

    public ExceptionTranslator(Throwable throwable) {
        this.throwable = throwable;
    }

    public void prettyPrint(PrintStream out) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (filter != null && filter.test(stackTraceElement)) {
                out.println(stackTraceElement);
            }
        }
    }

    public static void translate(Throwable throwable, PrintStream out) {
        new ExceptionTranslator(throwable).prettyPrint(out);
    }
}
