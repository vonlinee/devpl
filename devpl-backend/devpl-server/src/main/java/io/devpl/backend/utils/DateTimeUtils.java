package io.devpl.backend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String nowDateTimeString() {
        return format(new Date(), DATE_TIME_PATTERN);
    }

    public static String nowDateString() {
        return format(new Date(), DATE_PATTERN);
    }

    /**
     * 日期解析
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回Date
     */
    public static Date parseDate(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            // ignore exception
            return null;
        }
    }
}
