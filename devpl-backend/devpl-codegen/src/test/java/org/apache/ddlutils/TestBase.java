package org.apache.ddlutils;

import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Base class for DdlUtils tests.
 */
public abstract class TestBase {
    /**
     * The log for the tests.
     */
    private final Logger _log = LoggerFactory.getLogger(getClass());

    /**
     * Returns the log.
     *
     * @return The log
     */
    protected Logger getLog() {
        return _log;
    }

    /**
     * Parses the database defined in the given XML definition.
     *
     * @param dbDef The database XML definition
     * @return The database model
     */
    protected Database parseDatabaseFromString(String dbDef) {
        return DatabaseIO.read(dbDef);
    }

    /**
     * Compares the two strings but ignores any whitespace differences. It also
     * recognizes special delimiter chars.
     *
     * @param expected The expected string
     * @param actual   The actual string
     */
    protected void assertEqualsIgnoringWhitespaces(String expected, String actual) {
        String processedExpected = compressWhitespaces(expected);
        String processedActual = compressWhitespaces(actual);
        assertEquals(processedExpected, processedActual);
    }

    /**
     * Compresses the whitespaces in the given string to a single space. Also
     * recognizes special delimiter chars and removes whitespaces before them.
     *
     * @param original The original string
     * @return The resulting string
     */
    private String compressWhitespaces(String original) {
        StringBuilder result = new StringBuilder();
        char oldChar = ' ';
        char curChar;

        for (int idx = 0; idx < original.length(); idx++) {
            curChar = original.charAt(idx);
            if (Character.isWhitespace(curChar)) {
                if (oldChar != ' ') {
                    oldChar = ' ';
                    result.append(oldChar);
                }
            } else {
                if ((curChar == ',') || (curChar == ';') || (curChar == '(') || (curChar == ')')) {
                    if ((oldChar == ' ') && (!result.isEmpty())) {
                        // we're removing whitespaces before commas/semicolons
                        result.setLength(result.length() - 1);
                    }
                }
                if ((oldChar == ',') || (oldChar == ';')) {
                    // we're adding a space after commas/semicolons if necessary
                    result.append(' ');
                }
                result.append(curChar);
                oldChar = curChar;
            }
        }
        return result.toString();
    }

    protected void fail() {
        Assertions.fail();
    }

    protected void setName(String name) {

    }
}
