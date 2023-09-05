package org.apache.ddlutils;

/**
 * Indicates an attempted use of a name that was deemed illegal
 */
public class IllegalIdentifierException extends RuntimeException {
    public IllegalIdentifierException(String s) {
        super(s);
    }
}