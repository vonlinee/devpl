package org.apache.ddlutils.io.converters;

import java.sql.Timestamp;
import java.sql.Types;

/**
 * Converts between {@link java.sql.Timestamp} and {@link java.lang.String} using the standard
 * representation "yyyy-mm-dd hh:mm:ss.fffffffff".
 */
public class TimestampConverter implements SqlTypeConverter {

    @Override
    public Timestamp fromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (textRep == null || sqlTypeCode != Types.TIMESTAMP) {
            return null;
        }
        return Timestamp.valueOf(textRep);
    }
}
