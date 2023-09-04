package org.apache.ddlutils.io.converters;

import org.apache.ddlutils.util.Base64;

/**
 * Converts between a byte array and its Base64 encoded string representation (e.g. for use in XML).
 */
public class ByteArrayBase64Converter implements SqlTypeConverter {

    @Override
    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        try {
            return textRep == null ? null : Base64.decodeBase64(textRep.getBytes());
        } catch (Exception ex) {
            throw new ConversionException(ex);
        }
    }


    @Override
    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        try {
            return obj == null ? null : new String(Base64.encodeBase64((byte[]) obj));
        } catch (Exception ex) {
            throw new ConversionException(ex);
        }
    }
}
