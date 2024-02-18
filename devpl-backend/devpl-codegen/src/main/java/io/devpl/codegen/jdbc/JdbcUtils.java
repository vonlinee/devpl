package io.devpl.codegen.jdbc;

import io.devpl.codegen.core.CaseFormat;
import io.devpl.codegen.db.DBType;
import io.devpl.sdk.util.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * JDBC工具类
 */
public class JdbcUtils {

    /**
     * Constant that indicates an unknown (or unspecified) SQL type.
     *
     * @see java.sql.Types
     */
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;
    private static final Logger log = LoggerFactory.getLogger(JdbcUtils.class);
    /**
     * 过滤正则
     */
    private static final Pattern REGEX_TABLE_NAME = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");

    /**
     * 判断表名是否为正则表名(这表名规范比较随意,只能尽量匹配上特殊符号)
     *
     * @param tableName 表名
     * @return 是否正则
     */
    public static boolean matcherRegTable(String tableName) {
        return REGEX_TABLE_NAME.matcher(tableName).find();
    }

    /**
     * 判断数据库类型
     *
     * @param str url
     * @return 类型枚举值，如果没找到，则返回 null
     */
    public static DBType getDbType(String str) {
        str = str.toLowerCase();
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DBType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DBType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DBType.POSTGRE_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DBType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DBType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DBType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DBType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DBType.H2;
        } else if (str.contains(":kingbase:") || str.contains(":kingbase8:")) {
            return DBType.KINGBASE_ES;
        } else if (str.contains(":dm:")) {
            return DBType.DM;
        } else if (str.contains(":zenith:")) {
            return DBType.GAUSS;
        } else if (str.contains(":oscar:")) {
            return DBType.OSCAR;
        } else if (str.contains(":firebird:")) {
            return DBType.FIREBIRD;
        } else if (str.contains(":xugu:")) {
            return DBType.XU_GU;
        } else if (str.contains(":clickhouse:")) {
            return DBType.CLICK_HOUSE;
        } else if (str.contains(":sybase:")) {
            return DBType.SYBASE;
        } else {
            return DBType.OTHER;
        }
    }

    /**
     * 提取结果，并映射为JavaBean数据类型
     * 反射获取JavaBean的字段(不通过Introspection API)，无缓存，不支持父类字段映射
     *
     * @param resultSet   结果集合
     * @param constructor 提供一个空对象
     * @param <T>         行类型
     * @return list
     */
    public static <T> List<T> toBeans(ResultSet resultSet, Supplier<T> constructor) {
        final T firstRow = constructor.get();
        Class<?> rowType = firstRow.getClass();
        List<T> results = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Map<String, Field> fieldMap = new HashMap<>();
            Field[] declaredFields = rowType.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                fieldMap.put(CaseFormat.LOWER_UNDERSCORE.normalize(declaredField.getName()), declaredField);
            }
            // 遍历每列
            resultSet.next();
            results.add(populateBean(resultSet, rsmd, columnCount, fieldMap, firstRow));
            while (resultSet.next()) {
                results.add(populateBean(resultSet, rsmd, columnCount, fieldMap, constructor.get()));
            }
            return results;
        } catch (Exception exception) {
            log.error("cannot extract rows form ResultSet for type[{}]", rowType, exception);
        }
        return results;
    }

    /**
     * 填充对象的字段
     *
     * @param resultSet   结果集
     * @param rsmd        结果集元数据
     * @param columnCount 总列数
     * @param fieldMap    对象的字段列表
     * @param bean        对象
     * @param <T>         对象类型
     * @return 填充后的对象
     */
    private static <T> T populateBean(ResultSet resultSet, ResultSetMetaData rsmd, int columnCount, Map<String, Field> fieldMap, T bean) {
        try {
            for (int index = 1; index <= columnCount; index++) {
                // 列名称
                String column = lookupColumnName(rsmd, index);
                String property = replace(column).toLowerCase(Locale.US);
                Field field = fieldMap.get(property);
                if (!field.canAccess(bean)) {
                    field.setAccessible(true);
                }
                field.set(bean, getResultSetValue(resultSet, index, field.getType()));
            }
        } catch (SQLException | IllegalAccessException exception) {
            throw new RuntimeException("cannot populate bean [" + bean.getClass() + "]");
        }
        return bean;
    }

    /**
     * 获取数据库支持的表类型
     *
     * @param dbmd 数据库元数据
     * @return 表类型
     */
    public static List<String> getSupportedTableTypes(DatabaseMetaData dbmd) {
        List<String> tableTypes = new ArrayList<>();
        try {
            ResultSet tableTypesResultSet = dbmd.getTableTypes();
            while (tableTypesResultSet.next()) {
                tableTypes.add(tableTypesResultSet.getString("TABLE_TYPE"));
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return tableTypes;
    }

    /**
     * Replace all occurrences of a substring within a string with another string.
     *
     * @param inString {@code String} to examine
     * @return a {@code String} with the replacements
     */
    private static String replace(String inString) {
        int index = inString.indexOf(" ");
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = " ".length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            pos = index + patLen;
            index = inString.indexOf(" ", pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * Determine the column name to use. The column name is determined based on a
     * lookup using ResultSetMetaData.
     * <p>This method implementation takes into account recent clarifications
     * expressed in the JDBC 4.0 specification:
     * <p><i>columnLabel - the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the column</i>.
     *
     * @param resultSetMetaData the current meta-data to use
     * @param columnIndex       the index of the column for the lookup
     * @return the column name to use
     * @throws SQLException in case of lookup failure
     */
    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (name != null && !name.isBlank()) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

    /**
     * Check whether the given value can be treated as a String value.
     */
    public static boolean isStringValue(Class<?> inValueType) {
        // Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
        return (CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType));
    }

    /**
     * Check whether the given value is a {@code java.util.Date}
     * (but not one of the JDBC-specific subclasses).
     */
    public static boolean isDateValue(Class<?> inValueType) {
        return (java.util.Date.class.isAssignableFrom(inValueType) && !(java.sql.Date.class.isAssignableFrom(inValueType) || Time.class.isAssignableFrom(inValueType) || Timestamp.class.isAssignableFrom(inValueType)));
    }

    /**
     * Check whether the given SQL type is numeric.
     *
     * @param sqlType the SQL type to be checked
     * @return whether the type is numeric
     */
    public static boolean isNumeric(int sqlType) {
        return (Types.BIT == sqlType || Types.BIGINT == sqlType || Types.DECIMAL == sqlType ||
            Types.DOUBLE == sqlType || Types.FLOAT == sqlType || Types.INTEGER == sqlType ||
            Types.NUMERIC == sqlType || Types.REAL == sqlType || Types.SMALLINT == sqlType ||
            Types.TINYINT == sqlType);
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the specified value type.
     * <p>Uses the specifically typed ResultSet accessor methods, falling back to
     * {@link #getResultSetValue(java.sql.ResultSet, int)} for unknown types.
     * <p>Note that the returned value may not be assignable to the specified
     * required type, in case of an unknown type. Calling code needs to deal
     * with this case appropriately, e.g. throwing a corresponding exception.
     *
     * @param rs           is the ResultSet holding the data
     * @param index        is the column index
     * @param requiredType the required value type (maybe {@code null})
     * @return the value object (possibly not of the specified required type,
     * with further conversion steps necessary)
     * @throws SQLException if thrown by the JDBC API
     * @see #getResultSetValue(ResultSet, int)
     */
    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }
        Object value;
        // Explicitly extract typed value, as far as possible.
        if (String.class == requiredType) {
            return rs.getString(index);
        } else if (boolean.class == requiredType || Boolean.class == requiredType) {
            value = rs.getBoolean(index);
        } else if (byte.class == requiredType || Byte.class == requiredType) {
            value = rs.getByte(index);
        } else if (short.class == requiredType || Short.class == requiredType) {
            value = rs.getShort(index);
        } else if (int.class == requiredType || Integer.class == requiredType) {
            value = rs.getInt(index);
        } else if (long.class == requiredType || Long.class == requiredType) {
            value = rs.getLong(index);
        } else if (float.class == requiredType || Float.class == requiredType) {
            value = rs.getFloat(index);
        } else if (double.class == requiredType || Double.class == requiredType || Number.class == requiredType) {
            value = rs.getDouble(index);
        } else if (BigDecimal.class == requiredType) {
            return rs.getBigDecimal(index);
        } else if (java.sql.Date.class == requiredType) {
            return rs.getDate(index);
        } else if (java.sql.Time.class == requiredType) {
            return rs.getTime(index);
        } else if (java.sql.Timestamp.class == requiredType || java.util.Date.class == requiredType) {
            return rs.getTimestamp(index);
        } else if (byte[].class == requiredType) {
            return rs.getBytes(index);
        } else if (Blob.class == requiredType) {
            return rs.getBlob(index);
        } else if (Clob.class == requiredType) {
            return rs.getClob(index);
        } else if (requiredType.isEnum()) {
            // Enums can either be represented through a String or an enum index value:
            // leave enum type conversion up to the caller (e.g. a ConversionService)
            // but make sure that we return nothing other than a String or an Integer.
            Object obj = rs.getObject(index);
            if (obj instanceof String) {
                return obj;
            } else if (obj instanceof Number number) {
                // Defensively convert any Number to an Integer (as needed by our
                // ConversionService's IntegerToEnumConverterFactory) for use as index
                return NumberUtils.convertNumberToTargetClass(number, Integer.class);
            } else {
                // e.g. on Postgres: getObject returns a PGObject, but we need a String
                return rs.getString(index);
            }
        } else {
            // Some unknown type desired -> rely on getObject.
            try {
                return rs.getObject(index, requiredType);
            } catch (AbstractMethodError err) {
                log.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
            } catch (SQLFeatureNotSupportedException ex) {
                log.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
            } catch (SQLException ex) {
                log.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex);
            }

            // Corresponding SQL types for JSR-310 / Joda-Time types, left up
            // to the caller to convert them (e.g. through a ConversionService).
            String typeName = requiredType.getSimpleName();
            return switch (typeName) {
                case "LocalDate" -> rs.getDate(index);
                case "LocalTime" -> rs.getTime(index);
                case "LocalDateTime" -> rs.getTimestamp(index);
                default ->
                    // Fall back to getObject without type specification, again
                    // left up to the caller to convert the value if necessary.
                    getResultSetValue(rs, index);
            };
        }
        // Perform was-null check if necessary (for results that the JDBC driver returns as primitives).
        return (rs.wasNull() ? null : value);
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the most appropriate
     * value type. The returned value should be a detached value object, not having
     * any ties to the active ResultSet: in particular, it should not be a Blob or
     * Clob object but rather a byte array or String representation, respectively.
     * <p>Uses the {@code getObject(index)} method, but includes additional "hacks"
     * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
     * datatype and a {@code java.sql.Date} for DATE columns leaving out the
     * time portion: These columns will explicitly be extracted as standard
     * {@code java.sql.Timestamp} object.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     * @see java.sql.Blob
     * @see java.sql.Clob
     * @see java.sql.Timestamp
     */
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob blob) {
            obj = blob.getBytes(1, (int) blob.length());
        } else if (obj instanceof Clob clob) {
            obj = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            } else {
                obj = rs.getDate(index);
            }
        } else if (obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }

    /**
     * Convert a column name with underscores to the corresponding property name using "camel case".
     * A name like "customer_number" would match a "customerNumber" property name.
     *
     * @param name the column name to be converted
     * @return the name using "camel case"
     */
    public static String convertUnderscoreNameToPropertyName(String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && !name.isEmpty()) {
            if (name.length() > 1 && name.charAt(1) == '_') {
                result.append(Character.toUpperCase(name.charAt(0)));
            } else {
                result.append(Character.toLowerCase(name.charAt(0)));
            }
            for (int i = 1; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == '_') {
                    nextIsUpper = true;
                } else {
                    if (nextIsUpper) {
                        result.append(Character.toUpperCase(c));
                        nextIsUpper = false;
                    } else {
                        result.append(Character.toLowerCase(c));
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Determines whether a value for the specified column is present in the given result set.
     *
     * @param resultSet  The result set 未关闭的ResultSet
     * @param columnName 不为空
     * @return <code>true</code> if the column is present in the result set
     */
    public static boolean isColumnInResultSet(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int idx = 1; idx <= metaData.getColumnCount(); idx++) {
            if (columnName.equals(metaData.getColumnName(idx).toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
