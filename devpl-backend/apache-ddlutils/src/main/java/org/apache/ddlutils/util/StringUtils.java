package org.apache.ddlutils.util;

import java.util.Objects;

/**
 * Helper class containing string utility functions.
 */
public class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Compares the two given strings in a case-sensitive or insensitive manner
     * depending on the <code>caseSensitive</code> parameter.
     *
     * @param strA          The first string
     * @param strB          The second string
     * @param caseSensitive Whether case matters in the comparison
     * @return <code>true</code> if the two strings are equal
     */
    public static boolean equals(String strA, String strB, boolean caseSensitive) {
        return caseSensitive ? equals(strA, strB) : equalsIgnoreCase(strA, strB);
    }

    public static boolean equalsIgnoreCase(String s, String anotherString) {
        if (s == null) {
            return anotherString == null;
        }
        return s.equals(anotherString) || (anotherString != null)
                                          && (anotherString.length() == s.length())
                                          && s.regionMatches(true, 0, anotherString, 0, s.length());
    }

    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isEmpty(String[] str) {
        return str == null || str.length == 0;
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static String replace(String text, String repl, String with) {
        if (text == null) {
            return "";
        }
        return text.replace(repl, with);
    }

    public static String repeat(String str, int count) {
        if (isEmpty(str)) {
            return "";
        }
        return str.repeat(count);
    }

    public static String[] copy(String[] src) {
        if (src == null) {
            return EMPTY_STRING_ARRAY;
        }
        String[] dest = new String[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);
        return dest;
    }
}
