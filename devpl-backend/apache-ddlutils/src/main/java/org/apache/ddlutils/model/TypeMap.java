package org.apache.ddlutils.model;

import java.sql.Types;
import java.util.*;

/**
 * A class that maps SQL type names to their JDBC type ID found in
 * {@link java.sql.Types} and vice versa.
 */
public abstract class TypeMap {
    /**
     * The string representation of the {@link java.sql.Types#ARRAY} constant.
     */
    public static final String ARRAY = "ARRAY";
    /**
     * The string representation of the {@link java.sql.Types#BIGINT} constant.
     */
    public static final String BIGINT = "BIGINT";
    /**
     * The string representation of the {@link java.sql.Types#BINARY} constant.
     */
    public static final String BINARY = "BINARY";
    /**
     * The string representation of the {@link java.sql.Types#BIT} constant.
     */
    public static final String BIT = "BIT";
    /**
     * The string representation of the {@link java.sql.Types#BLOB} constant.
     */
    public static final String BLOB = "BLOB";
    /**
     * The string representation of the {@link java.sql.Types#BOOLEAN} constant.
     */
    public static final String BOOLEAN = "BOOLEAN";
    /**
     * The string representation of the {@link java.sql.Types#CHAR} constant.
     */
    public static final String CHAR = "CHAR";
    /**
     * The string representation of the {@link java.sql.Types#CLOB} constant.
     */
    public static final String CLOB = "CLOB";
    /**
     * The string representation of the {@link java.sql.Types#DATALINK} constant.
     */
    public static final String DATALINK = "DATALINK";
    /**
     * The string representation of the {@link java.sql.Types#DATE} constant.
     */
    public static final String DATE = "DATE";
    /**
     * The string representation of the {@link java.sql.Types#DECIMAL} constant.
     */
    public static final String DECIMAL = "DECIMAL";
    /**
     * The string representation of the {@link java.sql.Types#DISTINCT} constant.
     */
    public static final String DISTINCT = "DISTINCT";
    /**
     * The string representation of the {@link java.sql.Types#DOUBLE} constant.
     */
    public static final String DOUBLE = "DOUBLE";
    /**
     * The string representation of the {@link java.sql.Types#FLOAT} constant.
     */
    public static final String FLOAT = "FLOAT";
    /**
     * The string representation of the {@link java.sql.Types#INTEGER} constant.
     */
    public static final String INTEGER = "INTEGER";
    /**
     * The string representation of the {@link java.sql.Types#JAVA_OBJECT} constant.
     */
    public static final String JAVA_OBJECT = "JAVA_OBJECT";
    /**
     * The string representation of the {@link java.sql.Types#LONGVARBINARY} constant.
     */
    public static final String LONGVARBINARY = "LONGVARBINARY";
    /**
     * The string representation of the {@link java.sql.Types#LONGVARCHAR} constant.
     */
    public static final String LONGVARCHAR = "LONGVARCHAR";
    /**
     * The string representation of the {@link java.sql.Types#NULL} constant.
     */
    public static final String NULL = "NULL";
    /**
     * The string representation of the {@link java.sql.Types#NUMERIC} constant.
     */
    public static final String NUMERIC = "NUMERIC";
    /**
     * The string representation of the {@link java.sql.Types#OTHER} constant.
     */
    public static final String OTHER = "OTHER";
    /**
     * The string representation of the {@link java.sql.Types#REAL} constant.
     */
    public static final String REAL = "REAL";
    /**
     * The string representation of the {@link java.sql.Types#REF} constant.
     */
    public static final String REF = "REF";
    /**
     * The string representation of the {@link java.sql.Types#SMALLINT} constant.
     */
    public static final String SMALLINT = "SMALLINT";
    /**
     * The string representation of the {@link java.sql.Types#STRUCT} constant.
     */
    public static final String STRUCT = "STRUCT";
    /**
     * The string representation of the {@link java.sql.Types#TIME} constant.
     */
    public static final String TIME = "TIME";
    /**
     * The string representation of the {@link java.sql.Types#TIMESTAMP} constant.
     */
    public static final String TIMESTAMP = "TIMESTAMP";
    /**
     * The string representation of the {@link java.sql.Types#TINYINT} constant.
     */
    public static final String TINYINT = "TINYINT";
    /**
     * The string representation of the {@link java.sql.Types#VARBINARY} constant.
     */
    public static final String VARBINARY = "VARBINARY";
    /**
     * The string representation of the {@link java.sql.Types#VARCHAR} constant.
     */
    public static final String VARCHAR = "VARCHAR";
    /**
     * Maps type names to the corresponding {@link java.sql.Types} constants.
     */
    private static final Map<String, Integer> _typeNameToTypeCode = new HashMap<>();
    /**
     * Maps {@link java.sql.Types} type code constants to the corresponding type names.
     */
    private static final Map<Integer, String> _typeCodeToTypeName = new HashMap<>();
    /**
     * Conatins the types per category.
     */
    private static final Map<JdbcType, Set<Integer>> _typesPerCategory = new HashMap<>();

    static {
        registerJdbcType(Types.ARRAY, ARRAY, JdbcType.SPECIAL);
        registerJdbcType(Types.BIGINT, BIGINT, JdbcType.NUMERIC);
        registerJdbcType(Types.BINARY, BINARY, JdbcType.BINARY);
        registerJdbcType(Types.BIT, BIT, JdbcType.NUMERIC);
        registerJdbcType(Types.BLOB, BLOB, JdbcType.BINARY);
        registerJdbcType(Types.BOOLEAN, BOOLEAN, JdbcType.NUMERIC);
        registerJdbcType(Types.CHAR, CHAR, JdbcType.TEXTUAL);
        registerJdbcType(Types.CLOB, CLOB, JdbcType.TEXTUAL);
        registerJdbcType(Types.DATALINK, DATALINK, JdbcType.SPECIAL);
        registerJdbcType(Types.DATE, DATE, JdbcType.DATETIME);
        registerJdbcType(Types.DECIMAL, DECIMAL, JdbcType.NUMERIC);
        registerJdbcType(Types.DISTINCT, DISTINCT, JdbcType.SPECIAL);
        registerJdbcType(Types.DOUBLE, DOUBLE, JdbcType.NUMERIC);
        registerJdbcType(Types.FLOAT, FLOAT, JdbcType.NUMERIC);
        registerJdbcType(Types.INTEGER, INTEGER, JdbcType.NUMERIC);
        registerJdbcType(Types.JAVA_OBJECT, JAVA_OBJECT, JdbcType.SPECIAL);
        registerJdbcType(Types.LONGVARBINARY, LONGVARBINARY, JdbcType.BINARY);
        registerJdbcType(Types.LONGVARCHAR, LONGVARCHAR, JdbcType.TEXTUAL);
        registerJdbcType(Types.NULL, NULL, JdbcType.SPECIAL);
        registerJdbcType(Types.NUMERIC, NUMERIC, JdbcType.NUMERIC);
        registerJdbcType(Types.OTHER, OTHER, JdbcType.SPECIAL);
        registerJdbcType(Types.REAL, REAL, JdbcType.NUMERIC);
        registerJdbcType(Types.REF, REF, JdbcType.SPECIAL);
        registerJdbcType(Types.SMALLINT, SMALLINT, JdbcType.NUMERIC);
        registerJdbcType(Types.STRUCT, STRUCT, JdbcType.SPECIAL);
        registerJdbcType(Types.TIME, TIME, JdbcType.DATETIME);
        registerJdbcType(Types.TIMESTAMP, TIMESTAMP, JdbcType.DATETIME);
        registerJdbcType(Types.TINYINT, TINYINT, JdbcType.NUMERIC);
        registerJdbcType(Types.VARBINARY, VARBINARY, JdbcType.BINARY);
        registerJdbcType(Types.VARCHAR, VARCHAR, JdbcType.TEXTUAL);

        // Torque/Turbine extensions which we only support when reading from an XML schema
        _typeNameToTypeCode.put("BOOLEANINT", Types.TINYINT);
        _typeNameToTypeCode.put("BOOLEANCHAR", Types.CHAR);
    }

    /**
     * Returns all supported JDBC types.
     * @return The type codes ({@link java.sql.Types} constants)
     */
    public static int[] getSuportedJdbcTypes() {
        int[] typeCodes = new int[_typeCodeToTypeName.size()];
        int idx = 0;
        for (Iterator<Integer> it = _typeCodeToTypeName.keySet().iterator(); it.hasNext(); idx++) {
            typeCodes[idx] = it.next();
        }
        return typeCodes;
    }

    /**
     * Returns the JDBC type code (one of the {@link java.sql.Types} constants) that
     * corresponds to the given JDBC type name.
     * @param typeName The JDBC type name (case is ignored)
     * @return The type code or <code>null</code> if the type is unknown
     */
    public static Integer getJdbcTypeCode(String typeName) {
        return _typeNameToTypeCode.get(typeName.toUpperCase());
    }

    /**
     * Returns the JDBC type name that corresponds to the given type code
     * (one of the {@link java.sql.Types} constants).
     * @param typeCode The type code
     * @return The JDBC type name (one of the constants in this class) or
     * <code>null</code> if the type is unknown
     */
    public static String getJdbcTypeName(int typeCode) {
        return _typeCodeToTypeName.get(typeCode);
    }

    /**
     * Registers a jdbc type.
     * @param typeCode The type code (one of the {@link java.sql.Types} constants)
     * @param typeName The type name (case is ignored)
     * @param category The type category
     */
    protected static void registerJdbcType(int typeCode, String typeName, JdbcType category) {
        Integer typeId = typeCode;
        _typeNameToTypeCode.put(typeName.toUpperCase(), typeId);
        _typeCodeToTypeName.put(typeId, typeName.toUpperCase());
        Set<Integer> typesInCategory = _typesPerCategory.computeIfAbsent(category, k -> new HashSet<>());
        typesInCategory.add(typeId);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a numeric type.
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a numeric one
     */
    public static boolean isNumericType(int jdbcTypeCode) {
        Set<Integer> typesInCategory = _typesPerCategory.get(JdbcType.NUMERIC);
        return typesInCategory != null && typesInCategory.contains(jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a date/time type.
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a numeric one
     */
    public static boolean isDateTimeType(int jdbcTypeCode) {
        Set<Integer> typesInCategory = _typesPerCategory.get(JdbcType.DATETIME);
        return typesInCategory != null && typesInCategory.contains(jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a text type.
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a text one
     */
    public static boolean isTextType(int jdbcTypeCode) {
        Set<Integer> typesInCategory = _typesPerCategory.get(JdbcType.TEXTUAL);
        return typesInCategory != null && typesInCategory.contains(jdbcTypeCode);
    }

    /**
     * Determines whether the given jdbc type (one of the {@link java.sql.Types} constants)
     * is a binary type.
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a binary one
     */
    public static boolean isBinaryType(int jdbcTypeCode) {
        Set<Integer> typesInCategory = _typesPerCategory.get(JdbcType.BINARY);
        return typesInCategory != null && typesInCategory.contains(jdbcTypeCode);
    }

    /**
     * Determines whether the given sql type (one of the {@link java.sql.Types} constants)
     * is a special type.
     * @param jdbcTypeCode The type code
     * @return <code>true</code> if the type is a special one
     */
    public static boolean isSpecialType(int jdbcTypeCode) {
        Set<Integer> typesInCategory = _typesPerCategory.get(JdbcType.SPECIAL);
        return typesInCategory != null && typesInCategory.contains(jdbcTypeCode);
    }
}
