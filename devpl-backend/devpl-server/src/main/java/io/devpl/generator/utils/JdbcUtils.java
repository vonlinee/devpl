package io.devpl.generator.utils;

import com.baomidou.mybatisplus.generator.jdbc.DBType;
import io.devpl.codegen.utils.StringUtils;
import io.devpl.generator.jdbc.metadata.ResultSetColumnMetadata;
import org.apache.ibatis.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Jdbc工具类
 */
public class JdbcUtils {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static final Map<String, DBType> JDBC_DB_TYPE_CACHE = new ConcurrentHashMap<>();

    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    public static void loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot load driver class " + driverClassName + " becauese it does not exist!");
        }
    }

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
     * 不关闭 Connection,因为是从事务里获取的,sqlSession会负责关闭
     *
     * @param executor Executor
     * @return DBType
     */
    public static DBType DBType(Executor executor) {
        try {
            Connection conn = executor.getTransaction().getConnection();
            return JDBC_DB_TYPE_CACHE.computeIfAbsent(conn.getMetaData().getURL(), JdbcUtils::DBType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据连接地址判断数据库类型
     *
     * @param jdbcUrl 连接地址
     * @return ignore
     */
    public static DBType DBType(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            throw new RuntimeException("Error: The jdbcUrl is Null, Cannot read database type");
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DBType.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return DBType.MARIADB;
        } else if (url.contains(":oracle:")) {
            return DBType.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return DBType.SQL_SERVER2005;
        } else if (url.contains(":sqlserver2012:")) {
            return DBType.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return DBType.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return DBType.HSQL;
        } else if (url.contains(":db2:")) {
            return DBType.DB2;
        } else if (url.contains(":sqlite:")) {
            return DBType.SQLITE;
        } else if (url.contains(":h2:")) {
            return DBType.H2;
        } else if (regexFind(":dm\\d*:", url)) {
            return DBType.DM;
        } else if (url.contains(":xugu:")) {
            return DBType.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return DBType.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return DBType.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return DBType.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return DBType.GBASE;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DBType.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return DBType.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return DBType.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return DBType.OCEAN_BASE;
        } else if (url.contains(":highgo:")) {
            return DBType.HIGH_GO;
        } else if (url.contains(":cubrid:")) {
            return DBType.CUBRID;
        } else if (url.contains(":goldilocks:")) {
            return DBType.GOLDILOCKS;
        } else if (url.contains(":csiidb:")) {
            return DBType.CSIIDB;
        } else if (url.contains(":sap:")) {
            return DBType.SAP_HANA;
        } else if (url.contains(":impala:")) {
            return DBType.IMPALA;
        } else {
            logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return DBType.OTHER;
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
     * get metadata of result set
     *
     * @param resultSet ResultSet
     * @return list of ResultSetColumnMetadata
     * @throws SQLException errors when get the metadata of ResultSet
     */
    public static List<ResultSetColumnMetadata> getColumnMetadata(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int colCount = metaData.getColumnCount();
        List<ResultSetColumnMetadata> list = new ArrayList<>(colCount);
        for (int i = 1; i < colCount + 1; i++) {
            ResultSetColumnMetadata rscmd = new ResultSetColumnMetadata();
            rscmd.setColumnName(metaData.getColumnName(i));
            rscmd.setColumnLabel(metaData.getColumnLabel(i));
            rscmd.setColumnClassName(metaData.getColumnClassName(i));
            rscmd.setColumnType(metaData.getColumnType(i));
            rscmd.setTableName(metaData.getTableName(i));
            rscmd.setCatalogName(metaData.getCatalogName(i));
            rscmd.setColumnDisplaySize(metaData.getColumnDisplaySize(i));
            rscmd.setColumnTypeName(metaData.getColumnTypeName(i));
            rscmd.setPrecision(metaData.getPrecision(i));
            rscmd.setScale(metaData.getScale(i));
            rscmd.setSchemaName(metaData.getSchemaName(i));
            list.add(rscmd);
        }
        return list;
    }
}
