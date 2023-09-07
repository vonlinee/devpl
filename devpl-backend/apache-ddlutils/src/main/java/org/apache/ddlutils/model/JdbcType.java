package org.apache.ddlutils.model;

/**
 * Represents the different categories of jdbc types.
 */
public enum JdbcType {

    /**
     * The enum value for numeric jdbc types.
     */
    NUMERIC("numeric", 1),
    /**
     * The enum value for date/time jdbc types.
     */
    DATETIME("datetime", 2),
    /**
     * The enum value for textual jdbc types.
     */
    TEXTUAL("textual", 3),
    /**
     * The enum value for binary jdbc types.
     */
    BINARY("binary", 4),
    /**
     * The enum value for special jdbc types.
     */
    SPECIAL("special", 5),
    /**
     * The enum value for other jdbc types.
     */
    OTHER("other", 6);

    /**
     * The string representation of the Enum.
     */
    private final String name;
    private final int value;

    /**
     * Creates a new enum object.
     * @param defaultTextRep The textual representation
     * @param value          The corresponding integer value
     */
    JdbcType(String defaultTextRep, int value) {
        this.name = defaultTextRep;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
