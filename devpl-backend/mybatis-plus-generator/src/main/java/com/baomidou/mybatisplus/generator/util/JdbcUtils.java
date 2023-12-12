package com.baomidou.mybatisplus.generator.util;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @see org.springframework.jdbc.support.JdbcUtils
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
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()+\\\\\\[\\]|{};:'\",<.>?]+");

    /**
     * 判断表名是否为正则表名(这表名规范比较随意,只能尽量匹配上特殊符号)
     *
     * @param tableName 表名
     * @return 是否正则
     * @since 3.5.0
     */
    public static boolean matcherRegTable(@NotNull String tableName) {
        return REGX.matcher(tableName).find();
    }

    /**
     * 判断数据库类型
     *
     * @param str url
     * @return 类型枚举值，如果没找到，则返回 null
     */
    @NotNull
    public static DBType getDbType(@NotNull String str) {
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
     * 提取ResultSet只有单列的结果
     *
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
            log.error("cannot extract rows form ResultSet for type[{}]", rowType, exception);
        }
        return results;
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
