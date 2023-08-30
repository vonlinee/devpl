package com.baomidou.mybatisplus.generator.type;

import com.baomidou.mybatisplus.generator.jdbc.CommonJavaType;
import com.baomidou.mybatisplus.generator.utils.JdbcUtils;
import org.springframework.jdbc.core.SqlTypeValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @see JDBCType
 */
public class JDBCTypeAdapter implements TypeAdapter {

    private static final Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap<>(32);

    static {
        javaTypeToSqlTypeMap.put(boolean.class, Types.BOOLEAN);
        javaTypeToSqlTypeMap.put(Boolean.class, Types.BOOLEAN);
        javaTypeToSqlTypeMap.put(byte.class, Types.TINYINT);
        javaTypeToSqlTypeMap.put(Byte.class, Types.TINYINT);
        javaTypeToSqlTypeMap.put(short.class, Types.SMALLINT);
        javaTypeToSqlTypeMap.put(Short.class, Types.SMALLINT);
        javaTypeToSqlTypeMap.put(int.class, Types.INTEGER);
        javaTypeToSqlTypeMap.put(Integer.class, Types.INTEGER);
        javaTypeToSqlTypeMap.put(long.class, Types.BIGINT);
        javaTypeToSqlTypeMap.put(Long.class, Types.BIGINT);
        javaTypeToSqlTypeMap.put(BigInteger.class, Types.BIGINT);
        javaTypeToSqlTypeMap.put(float.class, Types.FLOAT);
        javaTypeToSqlTypeMap.put(Float.class, Types.FLOAT);
        javaTypeToSqlTypeMap.put(double.class, Types.DOUBLE);
        javaTypeToSqlTypeMap.put(Double.class, Types.DOUBLE);
        javaTypeToSqlTypeMap.put(BigDecimal.class, Types.DECIMAL);
        javaTypeToSqlTypeMap.put(LocalDate.class, Types.DATE);
        javaTypeToSqlTypeMap.put(LocalTime.class, Types.TIME);
        javaTypeToSqlTypeMap.put(LocalDateTime.class, Types.TIMESTAMP);
        javaTypeToSqlTypeMap.put(OffsetDateTime.class, Types.TIMESTAMP_WITH_TIMEZONE);
        javaTypeToSqlTypeMap.put(OffsetTime.class, Types.TIME_WITH_TIMEZONE);
        javaTypeToSqlTypeMap.put(java.sql.Date.class, Types.DATE);
        javaTypeToSqlTypeMap.put(java.sql.Time.class, Types.TIME);
        javaTypeToSqlTypeMap.put(java.sql.Timestamp.class, Types.TIMESTAMP);
        javaTypeToSqlTypeMap.put(Blob.class, Types.BLOB);
        javaTypeToSqlTypeMap.put(Clob.class, Types.CLOB);
    }


    /**
     * Derive a default SQL type from the given Java type.
     * @param javaType the Java type to translate
     * @return the corresponding SQL type, or {@link SqlTypeValue#TYPE_UNKNOWN} if none found
     */
    public static int javaTypeToSqlParameterType(Class<?> javaType) {
        if (javaType == null) {
            return SqlTypeValue.TYPE_UNKNOWN;
        }
        Integer sqlType = javaTypeToSqlTypeMap.get(javaType);
        if (sqlType != null) {
            return sqlType;
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return Types.NUMERIC;
        }
        if (JdbcUtils.isStringValue(javaType)) {
            return Types.VARCHAR;
        }
        if (JdbcUtils.isDateValue(javaType) || Calendar.class.isAssignableFrom(javaType)) {
            return Types.TIMESTAMP;
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean support(SQLType sqlType) {
        return sqlType instanceof JDBCType;
    }

    @Override
    public JavaType from(SQLType sqlType) {
        JDBCType jdbcType = (JDBCType) sqlType;
        return null;
    }

    @Override
    public boolean support(JavaType javaType) {
        return javaType instanceof CommonJavaType;
    }

    @Override
    public SQLType from(JavaType javaType) {
        CommonJavaType standardJavaType = (CommonJavaType) javaType;
        String qualifierName = standardJavaType.getQualifier();
        try {
            Class<?> clazz = Class.forName(qualifierName);
            int jdbcType = javaTypeToSqlParameterType(clazz);
            return JDBCType.valueOf(jdbcType);
        } catch (Exception exception) {

        }
        return JDBCType.VARCHAR;
    }
}
