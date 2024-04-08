package org.apache.ddlutils.model;


import org.apache.ddlutils.jdbc.JdbcTypeCategoryEnum;
import org.apache.ddlutils.util.BiMap;
import org.apache.ddlutils.util.MultiValueMap;

import java.sql.JDBCType;
import java.sql.Types;

/**
 * A class that maps SQL type names to their JDBC type ID found in
 * {@link java.sql.Types} and vice versa.
 */
public abstract class TypeMap {
    /**
     * Maps type names to the corresponding {@link java.sql.Types} constants.
     * Maps {@link java.sql.Types} type code constants to the corresponding type names.
     */
    private static final BiMap<String, Integer> _typeNameCodeMap = new BiMap<>();
    /**
     * Contains the types per category.
     */
    private static final MultiValueMap<JdbcTypeCategoryEnum, Integer> _typesPerCategory = new MultiValueMap<>();

    static {
        registerJdbcType(JDBCType.ARRAY, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.BIGINT, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.BINARY, JdbcTypeCategoryEnum.BINARY);
        registerJdbcType(JDBCType.BIT, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.BLOB, JdbcTypeCategoryEnum.BINARY);
        registerJdbcType(JDBCType.BOOLEAN, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.CHAR, JdbcTypeCategoryEnum.TEXTUAL);
        registerJdbcType(JDBCType.CLOB, JdbcTypeCategoryEnum.TEXTUAL);
        registerJdbcType(JDBCType.DATALINK, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.DATE, JdbcTypeCategoryEnum.DATETIME);
        registerJdbcType(JDBCType.DECIMAL, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.DISTINCT, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.DOUBLE, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.FLOAT, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.INTEGER, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.JAVA_OBJECT, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.LONGVARBINARY, JdbcTypeCategoryEnum.BINARY);
        registerJdbcType(JDBCType.LONGVARCHAR, JdbcTypeCategoryEnum.TEXTUAL);
        registerJdbcType(JDBCType.NULL, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.NUMERIC, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.OTHER, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.REAL, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.REF, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.SMALLINT, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.STRUCT, JdbcTypeCategoryEnum.SPECIAL);
        registerJdbcType(JDBCType.TIME, JdbcTypeCategoryEnum.DATETIME);
        registerJdbcType(JDBCType.TIMESTAMP, JdbcTypeCategoryEnum.DATETIME);
        registerJdbcType(JDBCType.TINYINT, JdbcTypeCategoryEnum.NUMERIC);
        registerJdbcType(JDBCType.VARBINARY, JdbcTypeCategoryEnum.BINARY);
        registerJdbcType(JDBCType.VARCHAR, JdbcTypeCategoryEnum.TEXTUAL);

        // Torque/Turbine extensions which we only support when reading from an XML schema
        _typeNameCodeMap.put("BOOLEANINT", Types.TINYINT);
        _typeNameCodeMap.put("BOOLEANCHAR", Types.CHAR);
    }

    /**
     * Returns all supported JDBC types.
     *
     * @return The type codes ({@link java.sql.Types} constants)
     */
    public static int[] getSupportedJdbcTypes() {
        return _typeNameCodeMap.values().stream().mapToInt(type -> type).toArray();
    }

    /**
     * Returns the JDBC type code (one of the {@link java.sql.Types} constants) that
     * corresponds to the given JDBC type name.
     *
     * @param typeName The JDBC type name (case is ignored)
     * @return The type code or <code>null</code> if the type is unknown
     */
    public static Integer getJdbcTypeCode(String typeName) {
        return _typeNameCodeMap.getValue(typeName.toUpperCase());
    }

    /**
     * Returns the JDBC type name that corresponds to the given type code
     * (one of the {@link java.sql.Types} constants).
     *
     * @param typeCode The type code
     * @return The JDBC type name (one of the constants in this class) or
     * <code>null</code> if the type is unknown
     */
    public static String getJdbcTypeName(int typeCode) {
        return _typeNameCodeMap.getKey(typeCode);
    }

    /**
     * Registers a jdbc type.
     *
     * @param jdbcType The type code and name (case is ignored) (one of the {@link java.sql.JDBCType} constants)
     * @param category The type category
     */
    public static void registerJdbcType(JDBCType jdbcType, JdbcTypeCategoryEnum category) {
        _typeNameCodeMap.put(jdbcType.name().toUpperCase(), jdbcType.getVendorTypeNumber());
        _typesPerCategory.addAll(category, jdbcType.getVendorTypeNumber());
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a numeric type.
     *
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a numeric one
     */
    public static boolean isNumericType(int jdbcTypeCode) {
        return _typesPerCategory.containsValue(JdbcTypeCategoryEnum.NUMERIC, jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a date/time type.
     *
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a numeric one
     */
    public static boolean isDateTimeType(int jdbcTypeCode) {
        return _typesPerCategory.containsValue(JdbcTypeCategoryEnum.DATETIME, jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a text type.
     *
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a text one
     */
    public static boolean isTextType(int jdbcTypeCode) {
        return _typesPerCategory.containsValue(JdbcTypeCategoryEnum.TEXTUAL, jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a binary type.
     *
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a binary one
     */
    public static boolean isBinaryType(int jdbcTypeCode) {
        return _typesPerCategory.containsValue(JdbcTypeCategoryEnum.BINARY, jdbcTypeCode);
    }

    /**
     * Determines whether the given sql type (one of the {@link java.sql.Types} constants)
     * is a special type.
     *
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a special one
     */
    public static boolean isSpecialType(int jdbcTypeCode) {
        return _typesPerCategory.containsValue(JdbcTypeCategoryEnum.SPECIAL, jdbcTypeCode);
    }
}
