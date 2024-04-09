package io.devpl.sdk.io;

import java.util.Objects;

/**
 * Enumeration of IO case sensitivity.
 * <p>
 * Different filing systems have different rules for case-sensitivity.
 * Windows is case-insensitive, Unix is case-sensitive.
 * </p>
 * <p>
 * This class captures that difference, providing an enumeration to
 * control how file name comparisons should be performed. It also provides
 * methods that use the enumeration to perform comparisons.
 * </p>
 * <p>
 * Wherever possible, you should use the {@code check} methods in this
 * class to compare file names.
 * </p>
 */
public enum IOCase {

    /**
     * The constant for case-sensitive regardless of operating system.
     */
    SENSITIVE("Sensitive", true),

    /**
     * The constant for case-insensitive regardless of operating system.
     */
    INSENSITIVE("Insensitive", false),

    /**
     * The constant for case sensitivity determined by the current operating system.
     * Windows is case-insensitive when comparing file names, Unix is case-sensitive.
     * <p>
     * <strong>Note:</strong> This only caters for Windows and Unix. Other operating
     * systems (e.g. OSX and OpenVMS) are treated as case-sensitive if they use the
     * Unix file separator and case-insensitive if they use the Windows file separator
     * (see {@link java.io.File#separatorChar}).
     * </p>
     * <p>
     * If you serialize this constant on Windows, and deserialize on Unix, or vice
     * versa, then the value of the case-sensitivity flag will change.
     * </p>
     */
    SYSTEM("System", !FilenameUtils.isSystemWindows());

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -6343169151696340687L;
    /**
     * The enumeration name.
     */
    private final String name;
    /**
     * The sensitivity flag.
     */
    private final transient boolean sensitive;

    /**
     * Constructs a new instance.
     *
     * @param name      the name
     * @param sensitive the sensitivity
     */
    IOCase(final String name, final boolean sensitive) {
        this.name = name;
        this.sensitive = sensitive;
    }

    /**
     * Tests for cases sensitivity in a null-safe manner.
     *
     * @param caseSensitivity an IOCase.
     * @return true if the input is non-null and {@link #isCaseSensitive()}.
     * @since 2.10.0
     */
    public static boolean isCaseSensitive(final IOCase caseSensitivity) {
        return caseSensitivity != null && !caseSensitivity.isCaseSensitive();
    }

    /**
     * Factory method to create an IOCase from a name.
     *
     * @param name the name to find
     * @return the IOCase object
     * @throws IllegalArgumentException if the name is invalid
     */
    public static IOCase forName(final String name) {
        for (final IOCase ioCase : IOCase.values()) {
            if (ioCase.getName().equals(name)) {
                return ioCase;
            }
        }
        throw new IllegalArgumentException("Invalid IOCase name: " + name);
    }

    /**
     * Replaces the enumeration from the stream with a real one.
     * This ensures that the correct flag is set for SYSTEM.
     *
     * @return the resolved object
     */
    private Object readResolve() {
        return forName(name);
    }

    /**
     * Gets the name of the constant.
     *
     * @return the name of the constant
     */
    public String getName() {
        return name;
    }

    /**
     * Does the object represent case-sensitive comparison.
     *
     * @return true if case-sensitive
     */
    public boolean isCaseSensitive() {
        return sensitive;
    }

    /**
     * Compares two strings using the case-sensitivity rule.
     * <p>
     * This method mimics {@link String#compareTo} but takes case-sensitivity
     * into account.
     * </p>
     *
     * @param str1 the first string to compare, not null
     * @param str2 the second string to compare, not null
     * @return int if equal using the case rules
     * @throws NullPointerException if either string is null
     */
    public int checkCompareTo(final String str1, final String str2) {
        Objects.requireNonNull(str1, "str1");
        Objects.requireNonNull(str2, "str2");
        return sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
    }

    /**
     * Compares two strings using the case-sensitivity rule.
     * <p>
     * This method mimics {@link String#equals} but takes case-sensitivity
     * into account.
     * </p>
     *
     * @param str1 the first string to compare, not null
     * @param str2 the second string to compare, not null
     * @return true if equal using the case rules
     * @throws NullPointerException if either string is null
     */
    public boolean checkEquals(final String str1, final String str2) {
        Objects.requireNonNull(str1, "str1");
        Objects.requireNonNull(str2, "str2");
        return sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
    }

    /**
     * Checks if one string starts with another using the case-sensitivity rule.
     * <p>
     * This method mimics {@link String#startsWith(String)} but takes case-sensitivity
     * into account.
     * </p>
     *
     * @param str   the string to check
     * @param start the start to compare against
     * @return true if equal using the case rules, false if either input is null
     */
    public boolean checkStartsWith(final String str, final String start) {
        return str != null && start != null && str.regionMatches(!sensitive, 0, start, 0, start.length());
    }

    /**
     * Checks if one string ends with another using the case-sensitivity rule.
     * <p>
     * This method mimics {@link String#endsWith} but takes case-sensitivity
     * into account.
     * </p>
     *
     * @param str the string to check
     * @param end the end to compare against
     * @return true if equal using the case rules, false if either input is null
     */
    public boolean checkEndsWith(final String str, final String end) {
        if (str == null || end == null) {
            return false;
        }
        final int endLen = end.length();
        return str.regionMatches(!sensitive, str.length() - endLen, end, 0, endLen);
    }

    /**
     * Checks if one string contains another starting at a specific index using the
     * case-sensitivity rule.
     * <p>
     * This method mimics parts of {@link String#indexOf(String, int)}
     * but takes case-sensitivity into account.
     * </p>
     *
     * @param str           the string to check, not null
     * @param strStartIndex the index to start at in str
     * @param search        the start to search for, not null
     * @return the first index of the search String,
     * -1 if no match or {@code null} string input
     * @throws NullPointerException if either string is null
     * @since 2.0
     */
    public int checkIndexOf(final String str, final int strStartIndex, final String search) {
        final int endIndex = str.length() - search.length();
        if (endIndex >= strStartIndex) {
            for (int i = strStartIndex; i <= endIndex; i++) {
                if (checkRegionMatches(str, i, search)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Checks if one string contains another at a specific index using the case-sensitivity rule.
     * <p>
     * This method mimics parts of {@link String#regionMatches(boolean, int, String, int, int)}
     * but takes case-sensitivity into account.
     * </p>
     *
     * @param str           the string to check, not null
     * @param strStartIndex the index to start at in str
     * @param search        the start to search for, not null
     * @return true if equal using the case rules
     * @throws NullPointerException if either string is null
     */
    public boolean checkRegionMatches(final String str, final int strStartIndex, final String search) {
        return str.regionMatches(!sensitive, strStartIndex, search, 0, search.length());
    }

    /**
     * Gets a string describing the sensitivity.
     *
     * @return a string describing the sensitivity
     */
    @Override
    public String toString() {
        return name;
    }
}
