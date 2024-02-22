package io.devpl.codegen.jdbc;

import io.devpl.codegen.jdbc.meta.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC接口的方式获取元数据
 *
 * @see DatabaseMetaData
 */
public class JdbcDatabaseMetadataLoader implements DatabaseMetadataLoader {

    Connection connection;
    DatabaseMetaData databaseMetaData;

    public JdbcDatabaseMetadataLoader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean setConnection(Connection connection) {
        this.connection = connection;
        return true;
    }

    private DatabaseMetaData getDatabaseMetaData() throws SQLException {
        if (databaseMetaData != null) {
            return databaseMetaData;
        }
        if (connection == null) {
            throw new SQLException("cannot get database metadata because connection is null");
        }
        if (connection.isClosed()) {
            throw new SQLException("cannot get database metadata because connection is closed");
        }
        return (databaseMetaData = connection.getMetaData());
    }

    /**
     * @return 数据库的catalog列表
     * @throws SQLException 数据库访问出错
     * @see java.sql.DatabaseMetaData#getCatalogs()
     */
    @Override
    public List<String> getCatalogs() throws SQLException {
        List<String> catalogs = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        try (ResultSet resultSet = metaData.getCatalogs()) {
            while (resultSet.next()) {
                catalogs.add(resultSet.getString(1));
            }
        }
        return catalogs;
    }

    /**
     * @param catalog          catalog
     * @param schemaPattern    schema, 对于mysql，schema和database可以理解为等价的.
     * @param tableNamePattern 表名称pattern
     * @param types            表类型 {@link JdbcDatabaseMetadataLoader#getTableTypes()}
     * @return 表元数据
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getTables(String, String, String, String[])
     */
    @Override
    public List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        List<TableMetadata> result = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        ;
        try (ResultSet resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            while (resultSet.next()) {
                TableMetadata tmd = new TableMetadata();
                tmd.initialize(resultSet);
                result.add(tmd);
            }
        }
        return result;
    }

    /**
     * @return 支持的表类型
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getTableTypes()
     */
    @Override
    public List<String> getTableTypes() throws SQLException {
        List<String> tableTypes = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        ;
        try (ResultSet tableTypesResultSet = metaData.getTableTypes()) {
            while (tableTypesResultSet.next()) {
                tableTypes.add(tableTypesResultSet.getString("TABLE_TYPE"));
            }
        }
        return tableTypes;
    }

    /**
     * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
     */
    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        List<ColumnMetadata> result = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        ;
        try (ResultSet resultSet = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
            while (resultSet.next()) {
                ColumnMetadata cmd = new ColumnMetadata();
                cmd.initialize(resultSet);
                result.add(cmd);
            }
        }
        return result;
    }

    /**
     * @param catalog catalog
     * @param schema  schema
     * @param table   表名
     * @return 主键信息列表
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getPrimaryKeys(String, String, String)
     */
    @Override
    public List<PrimaryKeyMetadata> getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        List<PrimaryKeyMetadata> result = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        ;
        try (ResultSet resultSet = metaData.getPrimaryKeys(catalog, schema, table)) {
            while (resultSet.next()) {
                PrimaryKeyMetadata pkm = new PrimaryKeyMetadata();
                pkm.initialize(resultSet);
                result.add(pkm);
            }
        }
        return result;
    }

    /**
     * @param catalog           catalog
     * @param schema            schema
     * @param table             table
     * @param columnNamePattern columnNamePattern
     * @return 列权限信息
     * @throws SQLException 数据库操作异常
     * @see java.sql.DatabaseMetaData#getColumnPrivileges(String, String, String, String)
     */
    @Override
    public List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        List<ColumnPrivilegesMetadata> result = new ArrayList<>();
        DatabaseMetaData metaData = getDatabaseMetaData();
        ;
        try (ResultSet resultSet = metaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
            while (resultSet.next()) {
                ColumnPrivilegesMetadata cpm = new ColumnPrivilegesMetadata();
                cpm.initialize(resultSet);
                result.add(cpm);
            }
        }
        return result;
    }
}
