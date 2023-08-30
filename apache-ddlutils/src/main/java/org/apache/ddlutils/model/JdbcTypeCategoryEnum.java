package org.apache.ddlutils.model;

/**
 * Represents the different categories of jdbc types.
 * @version $Revision: $
 */
public class JdbcTypeCategoryEnum {
    /**
     * The integer value for the enum value for numeric jdbc types.
     */
    public static final int VALUE_NUMERIC = 1;
    /**
     * The integer value for the enum value for date/time jdbc types.
     */
    public static final int VALUE_DATETIME = 2;
    /**
     * The integer value for the enum value for textual jdbc types.
     */
    public static final int VALUE_TEXTUAL = 3;
    /**
     * The integer value for the enum value for binary jdbc types.
     */
    public static final int VALUE_BINARY = 4;
    /**
     * The integer value for the enum value for special jdbc types.
     */
    public static final int VALUE_SPECIAL = 5;
    /**
     * The integer value for the enum value for all other jdbc types.
     */
    public static final int VALUE_OTHER = 6;

    /**
     * The enum value for numeric jdbc types.
     */
    public static final JdbcTypeCategoryEnum NUMERIC = new JdbcTypeCategoryEnum("numeric", VALUE_NUMERIC);
    /**
     * The enum value for date/time jdbc types.
     */
    public static final JdbcTypeCategoryEnum DATETIME = new JdbcTypeCategoryEnum("datetime", VALUE_DATETIME);
    /**
     * The enum value for textual jdbc types.
     */
    public static final JdbcTypeCategoryEnum TEXTUAL = new JdbcTypeCategoryEnum("textual", VALUE_TEXTUAL);
    /**
     * The enum value for binary jdbc types.
     */
    public static final JdbcTypeCategoryEnum BINARY = new JdbcTypeCategoryEnum("binary", VALUE_BINARY);
    /**
     * The enum value for special jdbc types.
     */
    public static final JdbcTypeCategoryEnum SPECIAL = new JdbcTypeCategoryEnum("special", VALUE_SPECIAL);
    /**
     * The enum value for other jdbc types.
     */
    public static final JdbcTypeCategoryEnum OTHER = new JdbcTypeCategoryEnum("other", VALUE_OTHER);

    /**
     * Version id for this class as relevant for serialization.
     */
    private static final long serialVersionUID = -2695615907467866410L;


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
    private JdbcTypeCategoryEnum(String defaultTextRep, int value) {
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
