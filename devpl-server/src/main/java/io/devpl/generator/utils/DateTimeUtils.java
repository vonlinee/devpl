package io.devpl.generator.utils;

import org.hibernate.sql.ast.spi.SqlAppender;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static final DateTimeFormatter DATE_TIME_FORMATTER_DATE = DateTimeFormatter.ofPattern(FORMAT_STRING_DATE, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIME_WITH_OFFSET = DateTimeFormatter.ofPattern(FORMAT_STRING_TIME_WITH_OFFSET, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIME = DateTimeFormatter.ofPattern(FORMAT_STRING_TIME, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS = DateTimeFormatter.ofPattern(FORMAT_STRING_TIMESTAMP_WITH_MILLIS, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP_WITH_MICROS = DateTimeFormatter.ofPattern(FORMAT_STRING_TIMESTAMP_WITH_MICROS, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS_AND_OFFSET = DateTimeFormatter.ofPattern(FORMAT_STRING_TIMESTAMP_WITH_MILLIS_AND_OFFSET, Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER_TIMESTAMP_WITH_MICROS_AND_OFFSET = DateTimeFormatter.ofPattern(FORMAT_STRING_TIMESTAMP_WITH_MICROS_AND_OFFSET, Locale.ENGLISH);

    private static final ThreadLocal<SimpleDateFormat> LOCAL_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_STRING_DATE, Locale.ENGLISH));
    private static final ThreadLocal<SimpleDateFormat> LOCAL_TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_STRING_TIME, Locale.ENGLISH));
    private static final ThreadLocal<SimpleDateFormat> TIME_WITH_OFFSET_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_STRING_TIME_WITH_OFFSET, Locale.ENGLISH));
    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_WITH_MILLIS_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_STRING_TIMESTAMP_WITH_MILLIS, Locale.ENGLISH));
    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_WITH_MICROS_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat(FORMAT_STRING_TIMESTAMP_WITH_MICROS, Locale.ENGLISH));

    public static void appendAsTimestampWithMillis(SqlAppender appender, TemporalAccessor temporalAccessor, boolean supportsOffset, TimeZone jdbcTimeZone) {
        if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)) {
            if (supportsOffset) {
                DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS_AND_OFFSET.formatTo(temporalAccessor, appender);
            } else {
                DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS.formatTo(LocalDateTime.ofInstant(Instant.from(temporalAccessor), jdbcTimeZone.toZoneId()), appender);
            }
        } else if (temporalAccessor instanceof Instant) {
            if (supportsOffset) {
                DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS_AND_OFFSET.formatTo(((Instant) temporalAccessor).atZone(jdbcTimeZone.toZoneId()), appender);
            } else {
                DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS.formatTo(LocalDateTime.ofInstant((Instant) temporalAccessor, jdbcTimeZone.toZoneId()), appender);
            }
        } else {
            DATE_TIME_FORMATTER_TIMESTAMP_WITH_MILLIS.formatTo(temporalAccessor, appender);
        }
    }

    /**
     * @deprecated Use {@link #appendAsLocalTime(SqlAppender, Date)} instead
     */
    @Deprecated
    public static void appendAsTime(SqlAppender appender, java.util.Date date) {
        appendAsLocalTime(appender, date);
    }

    public static void appendAsLocalTime(SqlAppender appender, Date date) {
        appender.appendSql(LOCAL_TIME_FORMAT.get().format(date));
    }

    /**
     * @deprecated Use {@link #appendAsLocalTime(SqlAppender, Calendar)} instead
     */
    @Deprecated
    public static void appendAsTime(SqlAppender appender, Calendar calendar) {
        appendAsLocalTime(appender, calendar);
    }

    public static void appendAsLocalTime(SqlAppender appender, Calendar calendar) {
        appender.appendSql(LOCAL_TIME_FORMAT.get().format(calendar.getTime()));
    }

    /**
     * 当前日期时间字符串
     * @param format 日期时间格式
     * @return 当前日期时间字符串
     */
    public static String stringOfNow(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
}
