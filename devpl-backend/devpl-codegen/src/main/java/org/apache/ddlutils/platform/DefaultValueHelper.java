package org.apache.ddlutils.platform;


import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.model.TypeMap;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Helper class for dealing with default values, e.g. converting them to other types.
 */
public class DefaultValueHelper {
    /**
     * Converts the given default value from the specified original to the target
     * jdbc type.
     *
     * @param defaultValue     The default value
     * @param originalTypeCode The original type code
     * @param targetTypeCode   The target type code
     * @return The converted default value
     */
    public String convert(String defaultValue, int originalTypeCode, int targetTypeCode) {
        String result = defaultValue;
        if (defaultValue != null) {
            switch (originalTypeCode) {
                case Types.BIT, Types.BOOLEAN -> result = convertBoolean(defaultValue, targetTypeCode).toString();
                case Types.DATE -> {
                    if (targetTypeCode == Types.TIMESTAMP) {
                        try {
                            Date date = Date.valueOf(result);
                            return new Timestamp(date.getTime()).toString();
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
                case Types.TIME -> {
                    if (targetTypeCode == Types.TIMESTAMP) {
                        try {
                            Time time = Time.valueOf(result);
                            return new Timestamp(time.getTime()).toString();
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Converts a boolean default value to the given target type.
     *
     * @param defaultValue   The default value
     * @param targetTypeCode The target type code
     * @return The converted value
     */
    private Object convertBoolean(String defaultValue, int targetTypeCode) {
        boolean value;
        Object result;
        try {
            // TODO 支持更多可以转换成boolean类型的值进行转换
            value = Boolean.parseBoolean(defaultValue);
        } catch (ConversionException ex) {
            return defaultValue;
        }
        if (targetTypeCode == Types.BIT || targetTypeCode == Types.BOOLEAN) {
            result = value;
        } else if (TypeMap.isNumericType(targetTypeCode)) {
            result = value ? 1 : 0;
        } else {
            result = Boolean.toString(value);
        }
        return result;
    }
}
