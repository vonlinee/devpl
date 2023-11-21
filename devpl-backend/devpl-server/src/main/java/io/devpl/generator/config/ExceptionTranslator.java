package io.devpl.generator.config;

import java.io.PrintStream;

public class ExceptionTranslator {

    private final Throwable throwable;

    public ExceptionTranslator(Throwable throwable) {
        this.throwable = throwable;
    }

    public void prettyPrint(PrintStream out) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            out.println(stackTrace[i]);
        }
    }

    public static void translate(Throwable throwable, PrintStream out) {
        new ExceptionTranslator(throwable).prettyPrint(out);
    }
}
