package io.devpl.codegen.jdbc;

import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.ColumnPrivilegesMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC接口的方式获取元数据
 */
public class JdbcDatabaseMetadataLoader implements DatabaseMetadataLoader {

    Connection connection;

    public JdbcDatabaseMetadataLoader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws RuntimeSQLException {
        List<TableMetadata> result = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
                while (resultSet.next()) {
                    TableMetadata tmd = new TableMetadata();
                    tmd.initialize(resultSet);
                    result.add(tmd);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return result;
    }

    @Override
    public List<String> getTableTypes() throws RuntimeSQLException {
        List<String> tableTypes = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tableTypesResultSet = metaData.getTableTypes()) {
                while (tableTypesResultSet.next()) {
                    tableTypes.add(tableTypesResultSet.getString("TABLE_TYPE"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return tableTypes;
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws RuntimeSQLException {
        List<ColumnMetadata> result = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)) {
                while (resultSet.next()) {
                    ColumnMetadata cmd = new ColumnMetadata();
                    cmd.initialize(resultSet);
                    result.add(cmd);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return result;
    }

    @Override
    public List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws RuntimeSQLException {
        List<ColumnPrivilegesMetadata> result = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getColumnPrivileges(catalog, schema, table, columnNamePattern)) {
                while (resultSet.next()) {
                    ColumnPrivilegesMetadata cpm = new ColumnPrivilegesMetadata();
                    cpm.initialize(resultSet);
                    result.add(cpm);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return result;
    }
}
