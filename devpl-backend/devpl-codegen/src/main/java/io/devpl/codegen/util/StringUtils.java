package io.devpl.codegen.util;

import java.util.regex.Pattern;

public class StringUtils {

    public static boolean stringContainsSpace(String s) {
        return s != null && s.indexOf(' ') != -1;
    }

    public static boolean hasText(String s) {
        return s != null && !s.isEmpty();
    }


    public static String composeFullyQualifiedTableName(String catalog,
                                                        String schema, String tableName, char separator) {
        StringBuilder sb = new StringBuilder();
        if (hasText(catalog)) {
            sb.append(catalog);
            sb.append(separator);
        }

        if (hasText(schema)) {
            sb.append(schema);
            sb.append(separator);
        } else {
            if (!sb.isEmpty()) {
                sb.append(separator);
            }
        }
        sb.append(tableName);

        return sb.toString();
    }

    public static boolean matches(String regex, String input) {
        if (null == regex || null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }


    public static boolean isTrue(String s) {
        return "true".equalsIgnoreCase(s); //$NON-NLS-1$
    }

    public static boolean stringContainsSQLWildcard(String s) {
        if (s == null) {
            return false;
        }
        return s.indexOf('%') != -1 || s.indexOf('_') != -1;
    }
}
