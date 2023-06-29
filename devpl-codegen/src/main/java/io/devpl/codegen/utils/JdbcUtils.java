package io.devpl.codegen.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * JDBC 工具类
 */
public class JdbcUtils {

    private static final Log logger = LogFactory.getLog(JdbcUtils.class);
    private static final Map<String, DbType> JDBC_DB_TYPE_CACHE = new ConcurrentHashMap<>();

    /**
     * 不关闭 Connection,因为是从事务里获取的,sqlSession会负责关闭
     * @param executor Executor
     * @return DbType
     */
    public static DbType getDbType(Executor executor) {
        try {
            Connection conn = executor.getTransaction().getConnection();
            return JDBC_DB_TYPE_CACHE.computeIfAbsent(conn.getMetaData().getURL(), JdbcUtils::getDbType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据连接地址判断数据库类型
     * @param jdbcUrl 连接地址
     * @return ignore
     */
    public static DbType getDbType(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            throw new RuntimeException("Error: The jdbcUrl is Null, Cannot read database type");
        }
        String url = jdbcUrl.toLowerCase();
        if (url.contains(":mysql:") || url.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (url.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (url.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (url.contains(":sqlserver:") || url.contains(":microsoft:")) {
            return DbType.SQL_SERVER2005;
        } else if (url.contains(":sqlserver2012:")) {
            return DbType.SQL_SERVER;
        } else if (url.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (url.contains(":hsqldb:")) {
            return DbType.HSQL;
        } else if (url.contains(":db2:")) {
            return DbType.DB2;
        } else if (url.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (url.contains(":h2:")) {
            return DbType.H2;
        } else if (regexFind(":dm\\d*:", url)) {
            return DbType.DM;
        } else if (url.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (regexFind(":kingbase\\d*:", url)) {
            return DbType.KINGBASE_ES;
        } else if (url.contains(":phoenix:")) {
            return DbType.PHOENIX;
        } else if (jdbcUrl.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (jdbcUrl.contains(":gbase:")) {
            return DbType.GBASE;
        } else if (jdbcUrl.contains(":gbasedbt-sqli:")) {
            return DbType.GBASEDBT;
        } else if (jdbcUrl.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (jdbcUrl.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (jdbcUrl.contains(":sybase:")) {
            return DbType.SYBASE;
        } else if (jdbcUrl.contains(":oceanbase:")) {
            return DbType.OCEAN_BASE;
        } else if (url.contains(":highgo:")) {
            return DbType.HIGH_GO;
        } else if (url.contains(":cubrid:")) {
            return DbType.CUBRID;
        } else if (url.contains(":goldilocks:")) {
            return DbType.GOLDILOCKS;
        } else if (url.contains(":csiidb:")) {
            return DbType.CSIIDB;
        } else if (url.contains(":sap:")) {
            return DbType.SAP_HANA;
        } else if (url.contains(":impala:")) {
            return DbType.IMPALA;
        } else {
            logger.warn("The jdbcUrl is " + jdbcUrl + ", Mybatis Plus Cannot Read Database type or The Database's Not Supported!");
            return DbType.OTHER;
        }
    }

    /**
     * 正则匹配
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
}
