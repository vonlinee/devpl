package org.apache.ddlutils.model;


import org.apache.ddlutils.DdlUtilsException;

/**
 * Indicates a model error.
 */
public class ModelException extends DdlUtilsException {

    /**
     * Creates a new empty exception object.
     */
    public ModelException() {
        super();
    }

    /**
     * Creates a new exception object.
     *
     * @param msg The exception message
     */
    public ModelException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception object.
     *
     * @param baseEx The base exception
     */
    public ModelException(Throwable baseEx) {
        super(baseEx);
    }

    /**
     * Creates a new exception object.
     *
     * @param msg    The exception message
     * @param baseEx The base exception
     */
    public ModelException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }
}
