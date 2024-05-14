package org.apache.ddlutils.io.converters;

import junit.framework.TestCase;

import java.sql.Time;
import java.sql.Types;
import java.util.Calendar;

/**
 * Tests the {@link TimeConverter}.
 */
public class TestTimeConverter extends TestCase {
    /**
     * The tested time converter.
     */
    private TimeConverter _timeConverter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _timeConverter = new TimeConverter();
    }

    @Override
    protected void tearDown() throws Exception {
        _timeConverter = null;
        super.tearDown();
    }

    /**
     * Tests a normal time string.
     */
    public void testNormalConvertFromHoursMinutesSecondsTimeString() {
        String textRep = "02:15:59";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.HOUR, 2);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 59);

        Time result = _timeConverter.fromString(textRep, Types.TIME);

        assertNotNull(result);
        assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a time string without seconds.
     */
    public void testNormalConvertFromHoursMinutesTimeString() {
        String textRep = "02:15";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.HOUR, 2);
        cal.set(Calendar.MINUTE, 15);

        Time result = _timeConverter.fromString(textRep, Types.TIME);

        assertNotNull(result);
        assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a time string with only an hour value.
     */
    public void testNormalConvertFromHoursTimeString() {
        String textRep = "02";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.HOUR, 2);

        Time result = _timeConverter.fromString(textRep, Types.TIME);

        assertNotNull(result);
        assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a full ISO datetime string.
     */
    public void testNormalConvertFromIsoDateTimeString() {
        String textRep = "2004-01-13 04:45:09.245";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.HOUR, 4);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 9);

        Time result = _timeConverter.fromString(textRep, Types.TIME);

        assertNotNull(result);
        assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests converting with an invalid SQL type.
     */
    public void testConvertFromStringWithInvalidSqlType() {
        String textRep = "02:15:59";
        Object result = _timeConverter.fromString(textRep, Types.INTEGER);

        assertNotNull(result);
        assertEquals(textRep, result);
    }

    /**
     * Tests converting a null.
     */
    public void testConvertFromStringWithNullTextRep() {
        Object result = _timeConverter.fromString(null, Types.TIME);

        assertNull(result);
    }

    /**
     * Tests converting an invalid time string.
     */
    public void testConvertFromStringWithInvalidTextRep() {
        String textRep = "99:99:99";

        try {
            _timeConverter.fromString(textRep, Types.TIME);
            fail("ConversionException expected");
        } catch (ConversionException ex) {
            // We expect the exception
        }
    }

    /**
     * Tests converting an invalid time string containing not only numbers.
     */
    public void testConvertFromStringWithAlphaTextRep() {
        String textRep = "aa:bb:cc";

        try {
            _timeConverter.fromString(textRep, Types.TIME);
            fail("ConversionException expected");
        } catch (ConversionException expected) {
            // We expect the exception
        }
    }

    /**
     * Tests converting a normal time to a string.
     */
    public void testNormalConvertToString() {
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(Calendar.HOUR, 2);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 59);

        Time time = new Time(cal.getTimeInMillis());
        String result = _timeConverter.toString(time, Types.TIME);

        assertNotNull(result);
        assertEquals("02:15:59", result);
    }

    /**
     * Tests converting a null time.
     */
    public void testConvertToStringWithNullTime() {
        Time time = null;
        String result = _timeConverter.toString(time, Types.TIME);

        assertNull(result);
    }

    /**
     * Tests converting a {@link java.util.Date}.
     */
    public void testConvertToStringWithWrongType() {
        java.util.Date date = new java.util.Date();

        try {
            _timeConverter.toString(date, Types.TIME);
            fail("ConversionException expected");
        } catch (ConversionException expected) {
            // We expect the exception
        }
    }
}
