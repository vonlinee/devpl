package org.apache.ddlutils.jdbc;

/**
 * Represents the different categories of jdbc types.
 */
public enum JdbcTypeCategoryEnum {

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

    private final String categoryName;
    private final int value;

    /**
     * Creates a new enum object.
     *
     * @param categoryName The textual representation
     * @param value        The corresponding integer value
     */
    JdbcTypeCategoryEnum(String categoryName, int value) {
        this.categoryName = categoryName;
        this.value = value;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getValue() {
        return value;
    }
}
