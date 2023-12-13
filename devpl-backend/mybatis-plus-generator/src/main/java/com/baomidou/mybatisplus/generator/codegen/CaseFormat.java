package com.baomidou.mybatisplus.generator.codegen;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 命名风格枚举
 *
 * @see com.google.common.base.CaseFormat
 */
public enum CaseFormat implements NamingStrategy {

    /**
     * 大写：所有字母全大写，非字母不变
     */
    UPPER() {
        /**
         * 所有字母全大写
         * @param source 源字符串
         * @return 大写
         */
        @Override
        public String normalize(String source) {
            return null;
        }

        @Override
        public boolean matches(String source) {
            if (source == null || source.isBlank()) {
                return false;
            }
            char[] charArray = source.toCharArray();
            for (char c : charArray) {
                if (!Character.isUpperCase(c)) {
                    return false;
                }
            }
            return true;
        }
    },

    /**
     * 所有字母全小写，非字母不变
     */
    LOWER() {
        /**
         * Returns a copy of the input string in which all {@linkplain Character#isUpperCase(char) uppercase ASCII
         * characters} have been converted to lowercase. All other characters are copied without
         * modification.
         */
        @Override
        public String normalize(String source) {
            int length = source.length();
            for (int i = 0; i < length; i++) {
                if (Character.isUpperCase(source.charAt(i))) {
                    char[] chars = source.toCharArray();
                    for (; i < length; i++) {
                        char c = chars[i];
                        if (Character.isUpperCase(c)) {
                            chars[i] = (char) (c ^ CASE_MASK);
                        }
                    }
                    return String.valueOf(chars);
                }
            }
            return source;
        }

        @Override
        public boolean matches(String source) {
            return false;
        }
    },

    /**
     * Hyphenated variable naming convention, e.g., "lower-hyphen".
     */
    LOWER_HYPHEN() {
        @Override
        public String normalize(String source) {
            return null;
        }

        @Override
        public boolean matches(String source) {
            return false;
        }

        @Override
        public String convert(CaseFormat fromStyle, String source) {
            if (fromStyle == LOWER_UNDERSCORE) {
                return source.replace('-', '_');
            }
            if (fromStyle == UPPER_UNDERSCORE) {
                return UPPER.normalize(source.replace('-', '_'));
            }
            return super.convert(fromStyle, source);
        }
    },

    /**
     * 首字母大写
     */
    CAPITAL_FIRST() {

        /**
         * 是否为大写命名
         */
        final Pattern CAPITAL_MODE = Pattern.compile("^[0-9A-Z/_]+$");

        @Override
        public String normalize(String source) {
            if (source == null || source.isEmpty()) {
                return source;
            }
            return Character.toUpperCase(source.charAt(0)) + source.substring(1);
        }

        @Override
        public boolean matches(String source) {
            if (source == null || source.isBlank()) {
                return false;
            }
            return CAPITAL_MODE.matcher(source).matches();
        }
    },

    /**
     * 首字母大写，其他均小写
     */
    CAPITAL_FIRST_ONLY() {
        @Override
        public String normalize(String source) {
            return source.isEmpty()
                ? source
                : Character.toUpperCase(source.charAt(0)) + LOWER.normalize(source.substring(1));
        }

        @Override
        public boolean matches(String source) {
            return false;
        }
    },

    /**
     * 驼峰形式(不管大小写)
     */
    CAMEL() {

        final Pattern CAMEL_CASE = Pattern.compile(".*[A-Z]+.*");

        @Override
        public String normalize(String source) {
            return null;
        }

        @Override
        public boolean matches(String source) {
            return CAMEL_CASE.matcher(source).matches();
        }
    },

    /**
     * 驼峰和下划线混合
     */
    CAMEL_UNDERLINE_MIXED() {
        @Override
        public String normalize(String source) {
            return null;
        }

        /**
         * 驼峰或者下划线混合
         * @param source 字符串
         * @return 是否同时包含驼峰和下划线
         */
        @Override
        public boolean matches(String source) {
            return CAMEL.matches(source) && UNDER_LINE.matches(source);
        }
    },

    /**
     * 首字母小写
     */
    LOWER_FIRST() {
        @Override
        public String normalize(String source) {
            if (source == null) {
                return "";
            }
            return source.substring(0, 1).toLowerCase() + source.substring(1);
        }

        @Override
        public boolean matches(String source) {
            if (source == null || source.isBlank()) {
                return false;
            }
            return Character.isLowerCase(source.charAt(0));
        }
    },

    /**
     * 下划线，不管大小写
     */
    UNDER_LINE() {

        final Pattern UNDER_LINE_CASE = Pattern.compile(".*[/_]+.*");

        @Override
        public String normalize(String source) {
            return null;
        }

        @Override
        public boolean matches(String source) {
            return UNDER_LINE_CASE.matcher(source).matches();
        }
    },

    /**
     * 小写下划线
     */
    LOWER_UNDERSCORE() {
        @Override
        public String normalize(String source) {
            StringBuilder result = new StringBuilder();
            result.append(Character.toLowerCase(source.charAt(0)));
            for (int i = 1; i < source.length(); i++) {
                char c = source.charAt(i);
                if (Character.isUpperCase(c)) {
                    result.append('_').append(Character.toLowerCase(c));
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }

        /**
         * TODO
         * @param source 字符串
         * @return 字符串是否符合小写下划线风格
         */
        @Override
        public boolean matches(String source) {
            return false;
        }
    },

    /**
     * 大写下划线
     */
    UPPER_UNDERSCORE() {
        @Override
        public String normalize(String source) {
            return LOWER_UNDERSCORE.normalize(source).toUpperCase(Locale.US);
        }

        @Override
        public boolean matches(String source) {
            return false;
        }
    };

    /**
     * A bit mask which selects the bit encoding ASCII character case.
     */
    private static final char CASE_MASK = 0x20;


    /**
     * 将目标字符串转换成当前命名风格
     *
     * @param source 源字符串
     * @return 转换结果
     */
    public abstract String normalize(String source);

    /**
     * 判断是否符合指定风格的命名格式
     *
     * @param source 字符串
     * @return 是否匹配此命名风格
     */
    public abstract boolean matches(String source);

    /**
     * 将字符串转换为指定的命名风格 由于性能原因，其他枚举值可以重写此方法
     *
     * @param s      原字符串
     * @param format 目标格式
     * @return 转换结果
     */
    public String convert(CaseFormat format, String s) {
        // deal with camel conversion
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                // include some extra space for separators
                out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
                out.append(format.normalize(s.substring(i, j)));
            } else {
                Objects.requireNonNull(out).append(format.normalize(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + wordSeparator.length();
        }
        return (i == 0) ? format.normalize(s) : Objects.requireNonNull(out).append(format.normalize(s.substring(i))).toString();
    }

    @Override
    public String apply(String source) {
        return normalize(source);
    }

    private String wordSeparator;
    private Matcher wordBoundary;

    /**
     * 字符匹配
     */
    private static class Matcher {

        public int indexIn(CharSequence sequence, int start) {
            return -1;
        }
    }

    // ================================ 静态工具方法 ===============================================

    /**
     * Indicates whether {@code c} is one of the twenty-six uppercase ASCII alphabetic characters
     * between {@code 'A'} and {@code 'Z'} inclusive. All others (including non-ASCII characters)
     * return {@code false}.
     */
    public static boolean isUpperCase(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    /**
     * 下划线转驼峰
     *
     * @param name 待转内容
     */
    public static String underlineToCamel(String name) {
        // 快速检查
        if (name == null || name.isBlank()) {
            // 没必要转换
            return "";
        }
        String tempName = name;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (CAPITAL_FIRST.matches(name) || CAMEL_UNDERLINE_MIXED.matches(name)) {
            tempName = name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = tempName.split("_");
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !isBlank(camel)).forEach(camel -> {
            if (result.isEmpty()) {
                // 第一个驼峰片段，首字母都小写
                result.append(LOWER_FIRST.normalize(camel));
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    /**
     * 去掉指定的前缀
     *
     * @param name   表名
     * @param prefix 前缀
     * @return 转换后的字符串
     */
    public static String removePrefix(String name, Set<String> prefix) {
        if (isBlank(name)) {
            return "";
        }
        // 判断是否有匹配的前缀，然后截取前缀
        return prefix.stream().filter(pf -> name.toLowerCase().startsWith(pf.toLowerCase())).findFirst().map(pf -> name.substring(pf.length())).orElse(name);
    }

    /**
     * 去掉下划线前缀并转成驼峰格式
     *
     * @param name   表名
     * @param prefix 前缀
     * @return 转换后的字符串
     */
    public static String removePrefixAndCamel(String name, Set<String> prefix) {
        return underlineToCamel(removePrefix(name, prefix));
    }

    /**
     * 去掉指定的后缀
     *
     * @param name   表名
     * @param suffix 后缀
     * @return 转换后的字符串
     */
    public static String removeSuffix(String name, Set<String> suffix) {
        if (isBlank(name)) {
            return "";
        }
        // 判断是否有匹配的后缀，然后截取后缀
        return suffix.stream().filter(sf -> name.toLowerCase().endsWith(sf.toLowerCase())).findFirst().map(sf -> name.substring(0, name.length() - sf.length())).orElse(name);
    }

    /**
     * 去掉下划线后缀并转成驼峰格式
     *
     * @param name   表名
     * @param suffix 后缀
     * @return 转换后的字符串
     */
    public static String removeSuffixAndCamel(String name, Set<String> suffix) {
        return underlineToCamel(removeSuffix(name, suffix));
    }

    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name) {
        if (!isBlank(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return "";
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     * 我们并不建议在数据库对应的对象中使用基本类型，因此这里不会考虑基本类型的情况
     */
    public static String getCapitalName(String name) {
        if (name.length() == 1) {
            return name.toUpperCase();
        }
        if (Character.isLowerCase(name.charAt(1))) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
        return name;
    }

    /**
     * 判断字符串中是否全是空白字符
     *
     * @param cs 需要判断的字符串
     * @return 如果字符串序列是 null 或者全是空白，返回 true
     */
    public static boolean isBlank(CharSequence cs) {
        if (cs != null) {
            int length = cs.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
