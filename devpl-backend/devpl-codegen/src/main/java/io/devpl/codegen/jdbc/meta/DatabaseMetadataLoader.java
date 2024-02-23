package io.devpl.codegen.jdbc.meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMetadataLoader extends AutoCloseable {

    /**
     * 设置数据库连接
     *
     * @param connection 数据库连接对象，必须是有效的连接
     * @return 是否设置成功
     */
    boolean setConnection(Connection connection);

    /**
     * 关系型数据库中没有catalog的概念
     *
     * @return catalogs in rdbms
     */
    List<String> getCatalogs() throws SQLException;

    /**
     * 获取所有数据库名称
     *
     * @return 所有数据库名称
     */
    List<String> getDatabaseNames() throws SQLException;

    /**
     * 获取表的元数据，是否包含列信息看具体实现
     *
     * @param catalog          catalog
     * @param schemaPattern    schema, 对于mysql，schema和database可以理解为等价的.
     * @param tableNamePattern 表名称pattern
     * @return table metadata list
     */
    List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException;

    /**
     * 获取数据库支持的表类型
     */
    List<String> getTableTypes() throws SQLException;

    /**
     * @return column metadata list
     */
    List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException;

    /**
     * @param catalog catalog
     * @param schema  schema
     * @param table   表名
     * @return 主键元数据
     */
    List<PrimaryKeyMetadata> getPrimaryKeys(String catalog, String schema, String table) throws SQLException;

    /**
     * 获取数据库的函数信息
     *
     * @param catalog             数据库目录
     * @param schemaPattern       数据库 schema
     * @param functionNamePattern 函数名称搜索值
     * @return 函数信息
     * @throws SQLException 访问数据库出错
     */
    List<FunctionMetadata> getFunctions(String catalog, String schemaPattern,
                                        String functionNamePattern) throws SQLException;

    /**
     * @param catalog           catalog
     * @param schema            schema
     * @param table             table
     * @param columnNamePattern columnNamePattern
     */
    List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException;

    /**
     * 获取SQL中的关键字列表
     *
     * @return 关键字列表
     */
    List<String> getSQLKeywords() throws SQLException;

    List<String> getDataTypes(String databaseName, String tableName) throws SQLException;

    /**
     * 数据清理操作
     */
    @Override
    default void close() {
    }
}
