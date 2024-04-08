package org.apache.ddlutils.io.converters;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.Date;
import java.sql.Types;
import java.util.Calendar;

/**
 * Tests the {@link DateConverter}.
 */
public class TestDateConverter {
    /**
     * The tested date converter.
     */
    private DateConverter _dateConverter;

    protected void setUp() throws Exception {
        _dateConverter = new DateConverter();
    }

    protected void tearDown() throws Exception {
        _dateConverter = null;
    }

    /**
     * Tests a normal date string.
     */
    @Test
    public void testNormalConvertFromYearMonthDateString() {
        String textRep = "2005-12-19";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(2005, Calendar.DECEMBER, 19);
        Date result = (Date) _dateConverter.fromString(textRep, Types.DATE);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a date string that has no day.
     */
    @Test
    public void testNormalConvertFromYearMonthString() {
        String textRep = "2005-12";
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(2005, Calendar.DECEMBER, 1);

        Date result = (Date) _dateConverter.fromString(textRep, Types.DATE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a date string that has only a year.
     */
    @Test
    public void testNormalConvertFromYearString() {
        String textRep = "2005";
        Calendar cal = Calendar.getInstance();

        cal.clear();
        cal.set(2005, Calendar.JANUARY, 1);

        Date result = (Date) _dateConverter.fromString(textRep, Types.DATE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests a full datetime string.
     */
    @Test
    public void testNormalConvertFromFullDateTimeString() {
        String textRep = "2005-06-07 10:11:12";
        Calendar cal = Calendar.getInstance();

        cal.clear();
        cal.set(2005, Calendar.JUNE, 7);

        Date result = (Date) _dateConverter.fromString(textRep, Types.DATE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(cal.getTimeInMillis(), result.getTime());
    }

    /**
     * Tests converting with an invalid SQL type.
     */
    @Test
    public void testConvertFromStringWithInvalidSqlType() {
        String textRep = "2005-12-19";
        Object result = _dateConverter.fromString(textRep, Types.INTEGER);

        // Make sure that the text representation is returned since SQL type was not a DATE
        Assertions.assertNotNull(result);
        Assertions.assertEquals(textRep, result);
    }

    /**
     * Tests handling of null.
     */
    @Test
    public void testConvertFromStringWithNullTextRep() {
        Object result = _dateConverter.fromString(null, Types.DATE);

        Assertions.assertNull(result);
    }

    /**
     * Tests an invalid date.
     */
    @Test
    public void testConvertFromStringWithInvalidTextRep() {
        String textRep = "9999-99-99";

        try {
            _dateConverter.fromString(textRep, Types.DATE);
            Assertions.fail("ConversionException expected");
        } catch (ConversionException ex) {
            // we expect the exception
        }
    }

    /**
     * Tests an invalid date that contains non-numbers.
     */
    @Test
    public void testConvertFromStringWithAlphaTextRep() {
        String textRep = "aaaa-bb-cc";

        try {
            _dateConverter.fromString(textRep, Types.DATE);
            Assertions.fail("ConversionException expected");
        } catch (ConversionException ex) {
            // we expect the exception
        }
    }

    /**
     * Tests converting a normal date to a string.
     */
    @Test
    public void testNormalConvertToString() {
        Calendar cal = Calendar.getInstance();

        cal.setLenient(false);
        cal.clear();
        cal.set(2005, Calendar.DECEMBER, 19);

        Date date = new Date(cal.getTimeInMillis());
        String result = _dateConverter.toString(date, Types.DATE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("2005-12-19", result);
    }

    /**
     * Tests converting a null.
     */
    @Test
    public void testConvertToStringWithNullDate() {
        Date date = null;
        String result = _dateConverter.toString(date, Types.DATE);

        Assertions.assertNull(result);
    }
}
