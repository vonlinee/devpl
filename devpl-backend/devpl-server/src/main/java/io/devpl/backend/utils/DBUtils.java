package io.devpl.backend.utils;

import io.devpl.sdk.util.StringUtils;
import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.apache.ddlutils.platform.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Jdbc工具类
 */
public class DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    /**
     * 从连接url字符串解析数据库名称
     *
     * @param url 连接url字符串
     * @return 数据库名称，可能为空
     */
    public static String getDatabaseNameFromConnectionUrl(String url) {
        int index = url.lastIndexOf("?");
        int i = index;
        while (url.charAt(i) != '/') {
            i--;
        }
        return url.substring(i + 1, index);
    }

    /**
     * 将数据提取成List<Map<String, Object>>
     *
     * @param resultSet ResultSet
     * @return List<Map < tring, Object>>
     */
    public static List<Map<String, Object>> extractMaps(ResultSet resultSet) {
        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> result = new ArrayList<>();
        int rowNum = 0;
        try {
            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet, rowNum++));
            }
        } catch (SQLException e) {
            logger.error("提取ResultSet失败", e);
        }
        return result;
    }

    /**
     * 提取单列的值，不关闭ResultSet
     *
     * @param resultSet ResultSet
     * @param <T>       该列数据类型
     * @return 该列的结果
     */
    public static <T> List<T> extractSingleColumn(ResultSet resultSet, Class<T> requiredType) {
        return extractSingleColumn(resultSet, new ArrayList<>());
    }

    /**
     * 提取单列的值，不关闭ResultSet
     *
     * @param resultSet ResultSet
     * @param result    存储结果的列表
     * @param <T>       该列数据类型
     * @return 该列的结果
     */
    public static <T> List<T> extractSingleColumn(ResultSet resultSet, List<T> result) {
        SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<>();
        int rowNum = 0;
        try {
            if (resultSet.isClosed()) {
                return result;
            }
            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet, rowNum++));
            }
        } catch (SQLException sqlException) {
            logger.info("error when extract single column of resultset", sqlException);
        }
        return result;
    }

    /**
     * 根据连接地址判断数据库类型
     *
     * @param jdbcUrl 连接地址
     * @return ignore
     */
    public static DatabaseType inferDBType(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            throw new RuntimeException("Error: The jdbcUrl is Null, Cannot read database type");
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return BuiltinDatabaseType.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return BuiltinDatabaseType.MARIADB;
        } else if (url.contains(":oracle:")) {
            return BuiltinDatabaseType.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return BuiltinDatabaseType.SQL_SERVER2005;
        } else if (url.contains(":sqlserver2012:")) {
            return BuiltinDatabaseType.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return BuiltinDatabaseType.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return BuiltinDatabaseType.HSQL;
        } else if (url.contains(":db2:")) {
            return BuiltinDatabaseType.DB2;
        } else if (url.contains(":sqlite:")) {
            return BuiltinDatabaseType.SQLITE;
        } else if (url.contains(":h2:")) {
            return BuiltinDatabaseType.H2;
        } else if (regexFind(":dm\\d*:", url)) {
            return BuiltinDatabaseType.DM;
        } else if (url.contains(":xugu:")) {
            return BuiltinDatabaseType.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return BuiltinDatabaseType.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return BuiltinDatabaseType.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return BuiltinDatabaseType.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return BuiltinDatabaseType.GBASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return BuiltinDatabaseType.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return BuiltinDatabaseType.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return BuiltinDatabaseType.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return BuiltinDatabaseType.OCEAN_BASE;
        } else if (url.contains(":highgo:")) {
            return BuiltinDatabaseType.HIGH_GO;
        } else if (url.contains(":cubrid:")) {
            return BuiltinDatabaseType.CUBRID;
        } else if (url.contains(":goldilocks:")) {
            return BuiltinDatabaseType.GOLDILOCKS;
        } else if (url.contains(":csiidb:")) {
            return BuiltinDatabaseType.CSIIDB;
        } else if (url.contains(":sap:")) {
            return BuiltinDatabaseType.SAP_HANA;
        } else if (url.contains(":impala:")) {
            return BuiltinDatabaseType.IMPALA;
        } else {
            logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return BuiltinDatabaseType.OTHER;
        }
    }

    /**
     * 正则匹配
     *
     * @param regex 正则
     * @param input 字符串
     * @return 验证成功返回 true，验证失败返回 false
     */
    public static boolean regexFind(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        return Pattern.compile(regex).matcher(input).find();
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
            logger.error("cannot extract rows form ResultSet for type[{}]", rowType, exception);
        }
        return results;
    }
}
