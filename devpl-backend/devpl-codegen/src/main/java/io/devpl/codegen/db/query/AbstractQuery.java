package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import org.jetbrains.annotations.Nullable;

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
     * 需要数据库名称，表名称，表注释信息
     */
    String getTableQuerySql(String catalog, String schemaName, @Nullable String tableName, boolean likeMatch);

    /**
     * 表名称 ResultSet 列名
     */
    String getTableNameResultSetColumnName();

    default String getDatabaseNameResultSetColumnName() {
        return null;
    }

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
