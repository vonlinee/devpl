package org.apache.ddlutils.io.converters;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * Converts between the various number types (including boolean types) and {@link java.lang.String}.
 * @version $Revision: 289996 $
 */
public class NumberConverter implements SqlTypeConverter {

    @Override
    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (textRep == null) {
            return null;
        } else {
            Object val;

            switch (sqlTypeCode) {
                case Types.BIGINT -> val = Long.parseLong(textRep);
                case Types.BIT, Types.BOOLEAN -> val = Boolean.parseBoolean(textRep);
                case Types.DECIMAL, Types.NUMERIC -> val = new BigDecimal(textRep);
                case Types.DOUBLE, Types.FLOAT -> val = Double.parseDouble(textRep);
                case Types.INTEGER -> val = Integer.parseInt(textRep);
                case Types.REAL -> val = Float.parseFloat(textRep);
                case Types.SMALLINT, Types.TINYINT -> val = Short.parseShort(textRep);
                default -> val = textRep;
            }

            return val;
        }
    }

    @Override
    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        if (obj == null) {
            return null;
        } else if (sqlTypeCode == Types.BIT) {
            return (Boolean) obj ? "1" : "0";
        } else {
            return obj.toString();
        }
    }
}
