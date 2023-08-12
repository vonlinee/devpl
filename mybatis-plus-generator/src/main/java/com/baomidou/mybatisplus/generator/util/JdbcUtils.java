package com.baomidou.mybatisplus.generator.util;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @see org.springframework.jdbc.support.JdbcUtils
 */
public class JdbcUtils {

    /**
     * Constant that indicates an unknown (or unspecified) SQL type.
     * @see java.sql.Types
     */
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    /**
     * 过滤正则
     */
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");

    /**
     * 判断表名是否为正则表名(这表名规范比较随意,只能尽量匹配上特殊符号)
     * @param tableName 表名
     * @return 是否正则
     * @since 3.5.0
     */
    public static boolean matcherRegTable(@NotNull String tableName) {
        return REGX.matcher(tableName).find();
    }

    /**
     * 判断数据库类型
     * @param str url
     * @return 类型枚举值，如果没找到，则返回 null
     */
    @NotNull
    public static DbType getDbType(@NotNull String str) {
        str = str.toLowerCase();
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DbType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DbType.H2;
        } else if (str.contains(":kingbase:") || str.contains(":kingbase8:")) {
            return DbType.KINGBASE_ES;
        } else if (str.contains(":dm:")) {
            return DbType.DM;
        } else if (str.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (str.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (str.contains(":firebird:")) {
            return DbType.FIREBIRD;
        } else if (str.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (str.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (str.contains(":sybase:")) {
            return DbType.SYBASE;
        } else {
            return DbType.OTHER;
        }
    }

    /**
     * 提取ResultSet只有单列的结果
     * @param resultSet    ResultSet
     * @param requiredType 该列的数据类型
     * @param <T>          该列的数据类型
     * @return 一列的结果
     */
    public static <T> List<T> extractSingleColumn(ResultSet resultSet, Class<T> requiredType) {
        SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<>(requiredType);
        try {
            List<T> list = new ArrayList<>(resultSet.getRow());
            int index = 0;
            while (resultSet.next()) {
                list.add(rowMapper.mapRow(resultSet, index++));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param resultSet 结果集合
     * @param rowType   行类型
     * @param <T>       行类型
     * @return list
     */
    public static <T> List<T> extractRows(ResultSet resultSet, Class<T> rowType) {
        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>(rowType);
        int index = 0;
        List<T> results = new ArrayList<>();
        try {
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet, index++));
            }
            return results;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return results;
    }

    /**
     * 查询
     * @param resultSetSupplier
     * @param rowType
     * @param <T>
     * @return
     */
    public static <T> List<T> extractRows(Supplier<ResultSet> resultSetSupplier, Class<T> rowType) {
        try (ResultSet resultSet = resultSetSupplier.get()) {
            return extractRows(resultSet, rowType);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Check whether the given value can be treated as a String value.
     */
    public static boolean isStringValue(Class<?> inValueType) {
        // Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
        return (CharSequence.class.isAssignableFrom(inValueType) ||
            StringWriter.class.isAssignableFrom(inValueType));
    }

    /**
     * Check whether the given value is a {@code java.util.Date}
     * (but not one of the JDBC-specific subclasses).
     */
    public static boolean isDateValue(Class<?> inValueType) {
        return (java.util.Date.class.isAssignableFrom(inValueType) &&
            !(java.sql.Date.class.isAssignableFrom(inValueType) ||
                java.sql.Time.class.isAssignableFrom(inValueType) ||
                java.sql.Timestamp.class.isAssignableFrom(inValueType)));
    }
}
