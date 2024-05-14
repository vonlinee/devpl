package org.apache.ddlutils.util;

/**
 * The base class of all runtime exceptions which can contain other
 * exceptions.
 *
 * @since 1.0
 */
public class NestableRuntimeException extends RuntimeException {

    /**
     * Holds the reference to the exception or error that caused
     * this exception to be thrown.
     */
    private Throwable cause = null;

    /**
     * Constructs a new <code>NestableRuntimeException</code> without specified
     * detail message.
     */
    public NestableRuntimeException() {
        super();
    }

    /**
     * Constructs a new <code>NestableRuntimeException</code> with specified
     * detail message.
     *
     * @param msg the error message
     */
    public NestableRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>NestableRuntimeException</code> with specified
     * nested <code>Throwable</code>.
     *
     * @param cause the exception or error that caused this exception to be
     *              thrown
     */
    public NestableRuntimeException(Throwable cause) {
        super();
        this.cause = cause;
    }

    /**
     * Constructs a new <code>NestableRuntimeException</code> with specified
     * detail message and nested <code>Throwable</code>.
     *
     * @param msg   the error message
     * @param cause the exception or error that caused this exception to be
     *              thrown
     */
    public NestableRuntimeException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * Returns the detail message string of this throwable. If it was
     * created with a null message, returns the following:
     * (cause==null ? null : cause.toString()).
     *
     * @return String message string of the throwable
     */
    @Override
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else if (cause != null) {
            return cause.toString();
        } else {
            return null;
        }
    }
}
