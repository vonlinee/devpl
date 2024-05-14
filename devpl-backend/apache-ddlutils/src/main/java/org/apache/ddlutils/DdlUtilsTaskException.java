package org.apache.ddlutils;

public class DdlUtilsTaskException extends DdlUtilsException {

    public DdlUtilsTaskException(Throwable cause) {
        super(cause);
    }

    public DdlUtilsTaskException(String message) {
        super(message);
    }

    public DdlUtilsTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
