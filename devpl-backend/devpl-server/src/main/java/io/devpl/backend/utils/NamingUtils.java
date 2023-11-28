package io.devpl.backend.utils;

import io.devpl.sdk.util.StringUtils;

/**
 * 命名工具类
 */
public class NamingUtils {

    /**
     * 表名转驼峰并移除前后缀
     *
     * @param upperFirst   首字母大写
     * @param tableName    表名
     * @param removePrefix 删除前缀
     * @param removeSuffix 删除后缀
     * @return java.lang.String
     */
    public static String camelCase(boolean upperFirst, String tableName, String removePrefix, String removeSuffix) {
        String className = tableName;
        // 移除前缀
        if (StringUtils.hasText(removePrefix)) {
            className = StringUtils.removePrefix(tableName, removePrefix);
        }
        // 移除后缀
        if (StringUtils.hasText(removeSuffix)) {
            className = StringUtils.removeSuffix(className, removeSuffix);
        }
        // 是否首字母大写
        return upperFirst ? toPascalCase(className) : toCamelCase(className);
    }

    /**
     * 将下划线方式命名的字符串转换为帕斯卡式。<br>
     * 规则为：
     * 单字之间不以空格或任何连接符断开
     * 第一个单字首字母采用大写字母
     * 后续单字的首字母亦用大写字母
     * 如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
     * 例如：hello_world=》HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toPascalCase(CharSequence name) {
        return upperFirst(toCamelCase(name));
    }

    /**
     * 大写首字母<br>
     * 例如：str = name, return Name
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperFirst(CharSequence str) {
        if (null == str) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + str.subSequence(1, str.length()).toString();
            }
        }
        return str.toString();
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
     * 规则为：
     * 单字之间不以空格或任何连接符断开
     * 第一个单字首字母采用小写字母
     * 后续单字的首字母亦用大写字母
     * 例如：hello_world=》helloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(CharSequence name) {
        return toCamelCase(name, '_');
    }

    /**
     * 将连接符方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     *
     * @param name   转换前的自定义方式命名的字符串
     * @param symbol 原字符串中的连接符连接符
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(CharSequence name, char symbol) {
        if (null == name) {
            return null;
        }
        final String name2 = name.toString();
        if (name2.indexOf(symbol) > -1) {
            final int length = name2.length();
            final StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);
                if (c == symbol) {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        }
        return name2;
    }
}
