package io.devpl.codegen.util;

import org.mybatis.generator.internal.util.StringUtility;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
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
        return "true".equalsIgnoreCase(s);
    }

    public static boolean stringContainsSQLWildcard(String s) {
        if (s == null) {
            return false;
        }
        return s.indexOf('%') != -1 || s.indexOf('_') != -1;
    }

    public static String escapeStringForJava(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"", true);
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) {
                sb.append("\\\"");
            } else {
                sb.append(token);
            }
        }

        return sb.toString();
    }

    public static String escapeStringForKotlin(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"$", true);
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) {
                sb.append("\\\"");
            } else if ("$".equals(token)) {
                sb.append("\\$");
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    /**
     * Given an input string, tokenize on the commas and trim all token.
     * Returns an empty set if the input string is null.
     *
     * @param in strong to tokenize.
     * @return Set of tokens
     */
    public static Set<String> tokenize(String in) {
        Set<String> answer = new HashSet<>();
        if (StringUtility.stringHasValue(in)) {
            StringTokenizer st = new StringTokenizer(in, ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (!s.isEmpty()) {
                    answer.add(s);
                }
            }
        }
        return answer;
    }

    /**
     * 校验字符串是否可以作为数据库名称或表名称。
     * 这是一个通用的校验方法，遵循大多数数据库系统的基本命名规则。
     *
     * @param name 要校验的字符串
     * @return 如果字符串是有效的数据库或表名称，则返回true；否则返回false。
     */
    public static boolean isValidDatabaseIdentifier(String name) {
        // 正则表达式：以字母或下划线开头，后面可以跟字母、数字或下划线
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        // 使用Pattern和Matcher类进行校验
        if (name == null || name.isEmpty()) {
            return false; // 空字符串或null不是有效的名称
        }
        // 检查名称长度是否超过通常的限制（例如，64个字符）
        // 注意：不同的数据库系统可能有不同的长度限制，这里只是一个通用的限制
        if (name.length() > 64) {
            return false; // 名称太长
        }
        // 使用正则表达式进行校验
        return name.matches(regex);
    }
}
