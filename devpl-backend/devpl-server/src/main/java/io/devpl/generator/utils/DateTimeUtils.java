package io.devpl.generator.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilities for dealing with date/times
 */
public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static final String FORMAT_STRING_DATE = "yyyy-MM-dd";
    public static final String FORMAT_STRING_TIME_WITH_OFFSET = "HH:mm:ssXXX";
    public static final String FORMAT_STRING_TIME = "HH:mm:ss";
    public static final String FORMAT_STRING_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_STRING_TIMESTAMP_WITH_MILLIS = FORMAT_STRING_TIMESTAMP + ".SSS";
    public static final String FORMAT_STRING_TIMESTAMP_WITH_MICROS = FORMAT_STRING_TIMESTAMP + ".SSSSSS";
    public static final String FORMAT_STRING_TIMESTAMP_WITH_MILLIS_AND_OFFSET = FORMAT_STRING_TIMESTAMP_WITH_MILLIS + "XXX";
    public static final String FORMAT_STRING_TIMESTAMP_WITH_MICROS_AND_OFFSET = FORMAT_STRING_TIMESTAMP_WITH_MICROS + "XXX";

    public static String stringOfNow() {
        return stringOfNow(FORMAT_STRING_TIMESTAMP);
    }

    /**
     * 取当前时间作为文件名
     *
     * @return 年与日时分秒
     */
    public static String nowForFilename() {
        return stringOfNow("yyyyMMddHHmmss");
    }

    /**
     * 当前日期时间字符串
     *
     * @param format 日期时间格式
     * @return 当前日期时间字符串
     */
    public static String stringOfNow(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
}
