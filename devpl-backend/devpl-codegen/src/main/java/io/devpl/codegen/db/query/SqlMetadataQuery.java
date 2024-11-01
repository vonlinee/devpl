package io.devpl.codegen.db.query;

import org.apache.ddlutils.platform.BuiltinDatabaseType;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

/**
 * Query
 */
public interface SqlMetadataQuery {

    /**
     * 数据库类型
     */
    BuiltinDatabaseType dbType();

    /**
     * 表信息查询 SQL
     * 需要数据库名称，表名称，表注释信息
     */
    String getTableQuerySql(String catalog, String schemaName, @Nullable String tableName, boolean likeMatch);

    /**
     * 表名称 ResultSet 列名
     */
    String getTableNameResultSetColumnName();

    /**
     * 表类型 ResultSet 列名
     *
     * @return 表类型 ResultSet 列名
     */
    default String getTableTypeResultSetColumnName() {
        return null;
    }

    /**
     * 数据库名称
     */
    String getDatabaseNameResultSetColumnName();

    /**
     * 表注释
     */
    String getTableCommentResultSetColumnName();

    /**
     * 表目录
     */
    String getTableCatalogResultSetColumnName();

    /**
     * 表字段信息查询 SQL
     */
    String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch);

    /**
     * 字段名称
     */
    String getColumnNameResultSetColumnName();

    /**
     * 字段类型
     */
    String getColumnDataTypeResultSetColumnName();

    /**
     * 字段注释
     */
    String getColumnCommentResultSetColumnName();

    /**
     * 主键字段
     */
    String getPrimaryKeyResultSetColumnName();

    /**
     * 获取所有数据库名称
     *
     * @return 所有数据库名称
     */
    List<String> getDatabaseNames() throws SQLException;

    /**
     * 获取数据类型列表
     *
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 数据类型列表
     * @throws SQLException SQLException
     */
    List<String> getDataTypes(String databaseName, String tableName) throws SQLException;
}
