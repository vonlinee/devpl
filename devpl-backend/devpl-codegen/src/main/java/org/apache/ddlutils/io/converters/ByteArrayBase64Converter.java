package org.apache.ddlutils.io.converters;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Converts between a byte array and its Base64 encoded string representation (e.g. for use in XML).
 */
public class ByteArrayBase64Converter implements SqlTypeConverter {

    @Override
    public Object fromString(String textRep, int sqlTypeCode) throws ConversionException {
        try {
            return textRep == null ? null : Base64.getDecoder().decode(textRep.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new ConversionException(ex);
        }
    }

    @Override
    public String toString(Object obj, int sqlTypeCode) throws ConversionException {
        if (obj instanceof byte[] bytes) {
            try {
                return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new ConversionException(ex);
            }
        }
        return null;
    }
}
