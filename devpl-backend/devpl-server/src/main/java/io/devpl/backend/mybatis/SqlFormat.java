package io.devpl.backend.mybatis;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;

public class SqlFormat {

    /**
     * mysql格式化
     * @param sql sql语句
     */
    public static String mysql(String sql) {
        return SQLUtils.formatMySql(sql);
    }

    /**
     * oracle格式化
     * @param sql sql语句
     */
    public static String oracleSql(String sql) {
        return SQLUtils.formatOracle(sql);
    }

    /**
     * pgsql格式化
     * @param sql sql语句
     */
    public static void PgSql(String sql) {
        System.out.println("postgreSql格式化：" + SQLUtils.format(sql, DbType.postgresql));
    }

    /**
     * sql格式
     * @param sql    格式化的语句
     * @param dbType 数据库类型
     */
    public static void sqlFormat(String sql, DbType dbType) {
        System.out.println("sql格式化：" + SQLUtils.format(sql, dbType));
    }
}
