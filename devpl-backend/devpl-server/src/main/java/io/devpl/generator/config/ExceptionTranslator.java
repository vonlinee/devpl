package io.devpl.generator.config;

import java.io.PrintWriter;

public class ExceptionTranslator {

    private final Throwable throwable;

    public ExceptionTranslator(Throwable throwable) {
        this.throwable = throwable;
    }

    public void prettyPrint(PrintWriter out) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {

        }
    }
}
