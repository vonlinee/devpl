package org.apache.ddlutils;

import org.apache.ddlutils.util.NestableRuntimeException;

/**
 * Base class for DdlUtils exceptions.
 */
public class DdlUtilsException extends NestableRuntimeException {

    /**
     * Creates a new empty exception object.
     */
    public DdlUtilsException() {
        super();
    }

    /**
     * Creates a new exception object.
     *
     * @param msg The exception message
     */
    public DdlUtilsException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception object.
     *
     * @param baseEx The base exception
     */
    public DdlUtilsException(Throwable baseEx) {
        super(baseEx);
    }

    /**
     * Creates a new exception object.
     *
     * @param msg    The exception message
     * @param baseEx The base exception
     */
    public DdlUtilsException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }

}
