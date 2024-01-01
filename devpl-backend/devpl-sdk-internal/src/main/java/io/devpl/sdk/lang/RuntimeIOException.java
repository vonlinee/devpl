package io.devpl.sdk.lang;

import java.io.IOException;

/**
 * 将IO异常包装为运行时异常
 *
 * @see IOException
 * @see java.io.FileNotFoundException
 */
public class RuntimeIOException extends RuntimeException {

    public RuntimeIOException(IOException exception) {
        super(exception);
    }
}