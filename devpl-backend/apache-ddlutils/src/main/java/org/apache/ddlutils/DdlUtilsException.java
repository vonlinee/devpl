package org.apache.ddlutils;

/**
 * Base class for DdlUtils exceptions.
 */
public class DdlUtilsException extends RuntimeException {
    /**
     * Constant for serializing instances of this class.
     */
    private static final long serialVersionUID = 5624776387174310551L;

    /**
     * Creates a new empty exception object.
     */
    public DdlUtilsException() {
        super();
    }

    /**
     * Creates a new exception object.
     * @param msg The exception message
     */
    public DdlUtilsException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception object.
     * @param baseEx The base exception
     */
    public DdlUtilsException(Throwable baseEx) {
        super(baseEx);
    }

    /**
     * Creates a new exception object.
     * @param msg    The exception message
     * @param baseEx The base exception
     */
    public DdlUtilsException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }

}
