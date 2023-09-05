package org.apache.ddlutils.io.converters;

/**
 * Interface for classes that convert between strings and sql data types.
 * @version $Revision: 289996 $
 */
public interface SqlTypeConverter {
    /**
     * Converts the given textual representation to an instance of the target type.
     * @param textRep     The textual representation
     * @param sqlTypeCode The target sql type code, one of the constants in {@link java.sql.Types}
     * @return The corresponding object
     */
    Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException;

    /**
     * Converts the given object to a string representation.
     * @param obj         The object
     * @param sqlTypeCode The corresponding source type code
     * @return The textual representation
     */
    String convertToString(Object obj, int sqlTypeCode) throws ConversionException;
}
