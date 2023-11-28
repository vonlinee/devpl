package io.devpl.backend.config.query;

import com.baomidou.mybatisplus.generator.jdbc.DBType;

/**
 * Query
 */
public interface AbstractQuery {

    /**
     * 数据库类型
     */
    DBType dbType();

    /**
     * 表信息查询 SQL
     */
    String getTableQuerySql(String tableName);

    /**
     * 表名称 ResultSet 列名
     */
    String getTableNameResultSetColumnName();

    /**
     * 表注释
     */
    String tableComment();

    /**
     * 表字段信息查询 SQL
     */
    String getTableFieldsQuerySql();

    /**
     * 字段名称
     */
    String fieldName();

    /**
     * 字段类型
     */
    String fieldType();

    /**
     * 字段注释
     */
    String fieldComment();

    /**
     * 主键字段
     */
    String fieldKey();
}
