package io.devpl.backend.common;

import java.io.PrintStream;

public interface ThrowablePrinter {

    void print(Throwable throwable, PrintStream stream);
}
