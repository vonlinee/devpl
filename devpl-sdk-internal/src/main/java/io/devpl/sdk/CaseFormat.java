package io.devpl.sdk;

import com.google.common.base.CharMatcher;
import com.google.common.base.Converter;
import io.devpl.sdk.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抄自谷歌的
 */
public enum CaseFormat {

    /**
     * 连字符变量命名方式，Hyphenated variable naming convention, e.g., "lower-hyphen".
     */
    LOWER_HYPHEN(CharMatcher.is('-'), "-") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }

        @Override
        String convert(CaseFormat format, String s) {
            if (format == LOWER_UNDERSCORE) {
                return s.replace('-', '_');
            }
            if (format == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s.replace('-', '_'));
            }
            return super.convert(format, s);
        }
    },

    /**
     * C++ variable naming convention, e.g., "lower_underscore".
     */
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }

        @Override
        String convert(CaseFormat format, String s) {
            if (format == LOWER_HYPHEN) {
                return s.replace('_', '-');
            }
            if (format == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s);
            }
            return super.convert(format, s);
        }
    },

    /**
     * Java variable naming convention, e.g., "lowerCamel".
     */
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(String word) {
            return firstCharOnlyToUpper(word);
        }

        @Override
        String normalizeFirstWord(String word) {
            return Ascii.toLowerCase(word);
        }
    },

    /**
     * Java and C++ class naming convention, e.g., "UpperCamel".
     */
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(String word) {
            return firstCharOnlyToUpper(word);
        }
    },

    /**
     * Java and C++ constant naming convention, e.g., "UPPER_UNDERSCORE".
     */
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(String word) {
            return Ascii.toUpperCase(word);
        }

        @Override
        String convert(CaseFormat format, String s) {
            if (format == LOWER_HYPHEN) {
                return Ascii.toLowerCase(s.replace('_', '-'));
            }
            if (format == LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(s);
            }
            return super.convert(format, s);
        }
    };

    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
        this.wordBoundary = wordBoundary;
        this.wordSeparator = wordSeparator;
    }

    /**
     * 只将首字母大写，同时将其他字母小写
     * @param word
     * @return String
     */
    public static String firstCharOnlyToUpper(String word) {
        return word.isEmpty() ? word : Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1));
    }

    /**
     * 数据表字段名转换为驼峰式名字的实体类属性名
     * @param tabAttr 数据表字段名
     * @return 转换后的驼峰式命名
     */
    public static String camelize(String tabAttr) {
        if (StringUtils.isBlank(tabAttr)) return tabAttr;
        Pattern pattern = Pattern.compile("(.*)_(\\w)(.*)");
        Matcher matcher = pattern.matcher(tabAttr);
        if (matcher.find()) {
            return camelize(matcher.group(1) + matcher.group(2).toUpperCase() + matcher.group(3));
        } else {
            return tabAttr;
        }
    }

    /**
     * 驼峰式的实体类属性名转换为数据表字段名
     * @param camelCaseStr 驼峰式的实体类属性名
     * @return 转换后的以"_"分隔的数据表字段名
     */
    public static String decamelize(String camelCaseStr) {
        return StringUtils.isBlank(camelCaseStr) ? camelCaseStr : camelCaseStr.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * Converts the specified {@code String str} from this format to the specified
     * {@code format}. A "best effort" approach is taken; if {@code str} does not
     * conform to the assumed format, then the behavior of this method is undefined,
     * but we make a reasonable effort at converting anyway.
     */
    public final String to(CaseFormat format, String str) {
        return (format == this) ? str : convert(format, str);
    }

    /**
     * Enum values can override for performance reasons.
     */
    String convert(CaseFormat format, String s) {
        // deal with camel conversion
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                // include some extra space for separators
                out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            } else {
                Objects.requireNonNull(out).append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + wordSeparator.length();
        }
        return (i == 0) ? format.normalizeFirstWord(s) : Objects.requireNonNull(out)
            .append(format.normalizeWord(s.substring(i)))
            .toString();
    }

    /**
     * Returns a {@code Converter} that converts strings from this format to
     * {@code targetFormat}.
     * @since 16.0
     */
    public Converter<String, String> converterTo(CaseFormat targetFormat) {
        return new StringConverter(this, targetFormat);
    }

    abstract String normalizeWord(String word);

    // normalize 规范化
    String normalizeFirstWord(String word) {
        return normalizeWord(word);
    }

    /**
     * 将传入的字段进行驼峰命名的验证（大驼峰）
     * @param field
     * @return
     */
    private boolean isFieldHump(String field) {
        int index = field.indexOf("_");
        String humps = field.substring(index + 1);
        String[] humpsList = humps.split("_");
        for (int i = 0; i < humpsList.length; i++) {
            if (!isRegularJudgment(humpsList[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证驼峰
     * @param field
     * @return
     */
    private boolean isRegularJudgment(String field) {
        String pattern = "^([A-Z][a-z0-9]+)+";
        return Pattern.matches(pattern, field);
    }

    private static final class StringConverter extends Converter<String, String> implements Serializable {

        private static final long serialVersionUID = 0L;
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;

        StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
            this.sourceFormat = Objects.requireNonNull(sourceFormat);
            this.targetFormat = Objects.requireNonNull(targetFormat);
        }

        @Override
        protected String doForward(String s) {
            return sourceFormat.to(targetFormat, s);
        }

        @Override
        protected String doBackward(String s) {
            return targetFormat.to(sourceFormat, s);
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof StringConverter that) {
                return sourceFormat.equals(that.sourceFormat) && targetFormat.equals(that.targetFormat);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return sourceFormat.hashCode() ^ targetFormat.hashCode();
        }

        @Override
        public String toString() {
            return sourceFormat + ".converterTo(" + targetFormat + ")";
        }
    }
}
