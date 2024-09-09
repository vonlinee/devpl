package io.devpl.sdk.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * 数字工具类
 */
public class NumberUtils {

    /**
     * <p>Convert a {@code String} to a {@code Integer}, handling
     * hex (0xhhhh) and octal (0dddd) notations.
     * N.B. a leading zero means octal; spaces are not trimmed.</p>
     *
     * <p>Returns {@code null} if the string is {@code null}.</p>
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code Integer} (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Integer decode(String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        try {
            return Integer.decode(str);
        } catch (Exception exception) {
            return defaultValue;
        }
    }

    /**
     * 解析字符串为int类型,解析失败返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return Integer 可以为null
     * @see NumberUtils#parseInt(String, int)
     */
    public static Integer parseInteger(final String str, Integer defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 解析字符串为int类型,解析失败返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return int
     */
    public static int parseInt(final String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 正则表达式将判断输入字符串是否为整数或者浮点数，涵盖负数的情况
     *
     * @param str 字符串
     * @return 是否为数值
     */
    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 判断是否是自然数
     *
     * @param str 字符串
     * @return 是否是自然数，整数 >= 0
     */
    public static boolean isNaturalNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                return false;
            }
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert the given number into an instance of the given target class.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the converted number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see java.lang.Byte
     * @see java.lang.Short
     * @see java.lang.Integer
     * @see java.lang.Long
     * @see java.math.BigInteger
     * @see java.lang.Float
     * @see java.lang.Double
     * @see java.math.BigDecimal
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                throwNumberOverflowException(number, targetClass);
            }
            return (T) Byte.valueOf(number.byteValue());
        } else if (Short.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                throwNumberOverflowException(number, targetClass);
            }
            return (T) Short.valueOf(number.shortValue());
        } else if (Integer.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throwNumberOverflowException(number, targetClass);
            }
            return (T) Integer.valueOf(number.intValue());
        } else if (Long.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            return (T) Long.valueOf(value);
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal bigDecimal) {
                // do not lose precision - use BigDecimal's own conversion
                return (T) bigDecimal.toBigInteger();
            } else {
                // original value is not a Big* number - use standard long conversion
                return (T) BigInteger.valueOf(number.longValue());
            }
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            // always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
            // (see BigDecimal javadoc for details)
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Raise an <em>overflow</em> exception for the given number and target class.
     *
     * @param number      the number we tried to convert
     * @param targetClass the target class we tried to convert to
     * @throws IllegalArgumentException if there is an overflow
     */
    private static void throwNumberOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    /**
     * Check for a {@code BigInteger}/{@code BigDecimal} long overflow
     * before returning the given number as a long value.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the long value, if convertible without overflow
     * @throws IllegalArgumentException if there is an overflow
     */
    private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
        BigInteger bigInt = null;
        if (number instanceof BigInteger bigInteger) {
            bigInt = bigInteger;
        } else if (number instanceof BigDecimal bigDecimal) {
            bigInt = bigDecimal.toBigInteger();
        }
        // Effectively analogous to JDK 8's BigInteger.longValueExact()
        if (bigInt != null && (bigInt.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0 || bigInt.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)) {
            throwNumberOverflowException(number, targetClass);
        }
        return number.longValue();
    }

    /**
     * 获取3个数中最小的值
     */
    public static int min(int one, int two, int three) {
        return (one = Math.min(one, two)) < three ? one : three;
    }

    /**
     * 小数转百分比字符串
     *
     * @param num           小数
     * @param decimalPlaces 保留小数位数
     * @return 百分比字符串，例如20.45%
     */
    public static String percentage(float num, int decimalPlaces) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##%");
        // 设置小数位数
        decimalFormat.setMaximumFractionDigits(decimalPlaces);
        decimalFormat.setMinimumFractionDigits(decimalPlaces);
        return decimalFormat.format(num * 100);
    }
}
