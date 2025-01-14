package org.apache.ddlutils;

import java.io.Serial;

/**
 * This exception is thrown when a database operation failed.
 */
public class DatabaseOperationException extends DdlUtilsException {
    /**
     * Constant for serializing instances of this class.
     */
    @Serial
    private static final long serialVersionUID = -3090677744278358036L;

    /**
     * Creates a new empty exception object.
     */
    public DatabaseOperationException() {
        super();
    }

    /**
     * Creates a new exception object.
     *
     * @param msg The exception message
     */
    public DatabaseOperationException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception object.
     *
     * @param baseEx The base exception
     */
    public DatabaseOperationException(Throwable baseEx) {
        super(baseEx);
    }

    /**
     * Creates a new exception object.
     *
     * @param msg    The exception message
     * @param baseEx The base exception
     */
    public DatabaseOperationException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}
