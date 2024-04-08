package org.apache.ddlutils.dynabean;


import org.apache.ddlutils.DdlUtilsException;

import java.io.Serial;

/**
 * This exception is thrown when something dealing with sql dyna beans or classes failed.
 */
public class SqlDynaException extends DdlUtilsException {
    /**
     * Constant for serializing instances of this class.
     */
    @Serial
    private static final long serialVersionUID = 7904337501884384392L;

    /**
     * Creates a new empty exception object.
     */
    public SqlDynaException() {
        super();
    }

    /**
     * Creates a new exception object.
     *
     * @param msg The exception message
     */
    public SqlDynaException(String msg) {
        super(msg);
    }

    /**
     * Creates a new exception object.
     *
     * @param baseEx The base exception
     */
    public SqlDynaException(Throwable baseEx) {
        super(baseEx);
    }

    /**
     * Creates a new exception object.
     *
     * @param msg    The exception message
     * @param baseEx The base exception
     */
    public SqlDynaException(String msg, Throwable baseEx) {
        super(msg, baseEx);
    }

}
