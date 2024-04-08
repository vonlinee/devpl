package io.devpl.backend.utils;

import io.devpl.codegen.db.DBTypeEnum;
import io.devpl.sdk.util.StringUtils;
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
    public static DBTypeEnum inferDBType(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            throw new RuntimeException("Error: The jdbcUrl is Null, Cannot read database type");
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DBTypeEnum.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return DBTypeEnum.MARIADB;
        } else if (url.contains(":oracle:")) {
            return DBTypeEnum.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return DBTypeEnum.SQL_SERVER2005;
        } else if (url.contains(":sqlserver2012:")) {
            return DBTypeEnum.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return DBTypeEnum.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return DBTypeEnum.HSQL;
        } else if (url.contains(":db2:")) {
            return DBTypeEnum.DB2;
        } else if (url.contains(":sqlite:")) {
            return DBTypeEnum.SQLITE;
        } else if (url.contains(":h2:")) {
            return DBTypeEnum.H2;
        } else if (regexFind(":dm\\d*:", url)) {
            return DBTypeEnum.DM;
        } else if (url.contains(":xugu:")) {
            return DBTypeEnum.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return DBTypeEnum.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return DBTypeEnum.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return DBTypeEnum.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return DBTypeEnum.GBASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DBTypeEnum.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return DBTypeEnum.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return DBTypeEnum.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return DBTypeEnum.OCEAN_BASE;
        } else if (url.contains(":highgo:")) {
            return DBTypeEnum.HIGH_GO;
        } else if (url.contains(":cubrid:")) {
            return DBTypeEnum.CUBRID;
        } else if (url.contains(":goldilocks:")) {
            return DBTypeEnum.GOLDILOCKS;
        } else if (url.contains(":csiidb:")) {
            return DBTypeEnum.CSIIDB;
        } else if (url.contains(":sap:")) {
            return DBTypeEnum.SAP_HANA;
        } else if (url.contains(":impala:")) {
            return DBTypeEnum.IMPALA;
        } else {
            logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return DBTypeEnum.OTHER;
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
