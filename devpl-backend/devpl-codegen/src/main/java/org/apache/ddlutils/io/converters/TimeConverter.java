package org.apache.ddlutils.io.converters;

import org.apache.ddlutils.DdlUtilsException;

import java.sql.Time;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Converts between {@link java.sql.Time} and {@link java.lang.String} using the standard
 * representation "hh:mm:ss".
 */
public class TimeConverter implements SqlTypeConverter {
    /**
     * The regular expression pattern for the parsing of ISO times.
     */
    private final Pattern _timePattern;
    /**
     * The calendar object to convert to/from times.
     */
    private final Calendar _calendar;

    /**
     * Creates a new time converter object.
     */
    public TimeConverter() {
        try {
            _timePattern = Pattern.compile("(?:\\d{4}-\\d{2}-\\d{2}\\s)?(\\d{2})(?::(\\d{2}))?(?::(\\d{2}))?(?:\\..*)?");
        } catch (PatternSyntaxException ex) {
            throw new DdlUtilsException(ex);
        }

        _calendar = Calendar.getInstance();
        _calendar.setLenient(false);
    }

    /**
     * @param sqlTypeCode 92
     * @see java.sql.Types#TIME
     */
    @Override
    public Time fromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (textRep != null) {
            // we're not using {@link java.sql.Time#valueOf(String)} as this method is too strict
            // it only parses the full spec "hh:mm:ss"
            Matcher matcher = _timePattern.matcher(textRep);
            int hours;
            int minutes = 0;
            int seconds = 0;

            if (matcher.matches()) {
                int numGroups = matcher.groupCount();
                try {
                    hours = Integer.parseInt(matcher.group(1));
                    if ((numGroups >= 2) && (matcher.group(2) != null)) {
                        minutes = Integer.parseInt(matcher.group(2));
                    }
                    if ((numGroups >= 3) && (matcher.group(3) != null)) {
                        seconds = Integer.parseInt(matcher.group(3));
                    }
                } catch (NumberFormatException ex) {
                    throw new ConversionException("Not a valid time : " + textRep, ex);
                }
                _calendar.clear();
                try {
                    _calendar.set(Calendar.HOUR_OF_DAY, hours);
                    _calendar.set(Calendar.MINUTE, minutes);
                    _calendar.set(Calendar.SECOND, seconds);
                    return new Time(_calendar.getTimeInMillis());
                } catch (IllegalArgumentException ex) {
                    throw new ConversionException("Not a valid time : " + textRep, ex);
                }
            }
            throw new ConversionException("Not a valid time : " + textRep);
        }
        return null;
    }
}
