package org.apache.ddlutils.io.converters;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * Converts between the various number types (including boolean types) and {@link java.lang.String}.
 */
public class NumberConverter implements SqlTypeConverter {

    @Override
    public Number fromString(String val, int sqlTypeCode) throws ConversionException {
        if (val == null) {
            return null;
        }
        try {
            return switch (sqlTypeCode) {
                case Types.BIGINT -> Long.valueOf(val);
                case Types.BIT, Types.BOOLEAN -> {
                    if ("true".equals(val)) {
                        yield 1;
                    } else if ("false".equals(val)) {
                        yield 0;
                    }
                    yield -1;
                }
                case Types.DECIMAL, Types.NUMERIC -> new BigDecimal(val);
                case Types.DOUBLE, Types.FLOAT -> Double.valueOf(val);
                case Types.INTEGER -> Integer.valueOf(val);
                case Types.REAL -> Float.valueOf(val);
                case Types.SMALLINT, Types.TINYINT -> Short.valueOf(val);
                default -> null;
            };
        } catch (Exception exception) {
            throw new ConversionException(exception);
        }
    }

    @Override
    public String toString(Object obj, int sqlTypeCode) throws ConversionException {
        if (obj == null) {
            return null;
        } else if (sqlTypeCode == Types.BIT && obj instanceof Number num) {
            return num.intValue() > 0 ? "1" : "0";
        } else {
            return obj.toString();
        }
    }
}
