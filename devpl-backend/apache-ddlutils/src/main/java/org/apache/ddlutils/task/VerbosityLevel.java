package org.apache.ddlutils.task;

/**
 * Helper class that defines the possible values for the verbosity attribute.
 * @version $Revision: $
 * ignore="true"
 */
public class VerbosityLevel {
    /**
     * The possible levels.
     */
    private static final String[] LEVELS = {"Fatal".toUpperCase(),
        "ERROR".toUpperCase(),
        "WARN".toUpperCase(),
        "INFO".toUpperCase(),
        "DEBUG".toUpperCase(),
        "FATAL".toLowerCase(),
        "ERROR".toLowerCase(),
        "WARN".toLowerCase(),
        "INFO".toLowerCase(),
        "DEBUG".toLowerCase()};
    private String value;

    /**
     * Creates an uninitialized verbosity level object.
     */
    public VerbosityLevel() {
        super();
    }

    /**
     * Creates an initialized verbosity level object.
     * @param level The level
     */
    public VerbosityLevel(String level) {
        super();
        setValue(level);
    }

    public String[] getValues() {
        String[] result = new String[LEVELS.length];

        System.arraycopy(LEVELS, 0, result, 0, LEVELS.length);
        return result;
    }

    /**
     * Determines whether this is DEBUG verbosity.
     * @return <code>true</code> if this is the DEBUG level
     */
    public boolean isDebug() {
        return "DEBUG".equalsIgnoreCase(getValue());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
