package io.devpl.backend.config.query;

import io.devpl.codegen.jdbc.DatabaseMetadataLoader;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.codegen.jdbc.meta.ColumnPrivilegesMetadata;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import io.devpl.sdk.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbstractQueryDatabaseMetadataLoader implements DatabaseMetadataLoader {

    AbstractQuery query;
    Connection connection;

    public AbstractQueryDatabaseMetadataLoader(Connection connection, AbstractQuery query) {
        this.connection = connection;
        this.query = query;
    }

    @Override
    public List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws RuntimeSQLException {
        List<TableMetadata> tableList = new ArrayList<>();
        String tableQuerySql = query.getTableQuerySql(tableNamePattern, true);
        try (PreparedStatement preparedStatement = connection.prepareStatement(tableQuerySql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                // 查询数据
                while (rs.next()) {
                    String tableName = rs.getString(query.getTableNameResultSetColumnName());
                    if (StringUtils.hasText(tableNamePattern) && !tableName.contains(tableNamePattern)) {
                        continue;
                    }
                    TableMetadata table = new TableMetadata();
                    if (query.getDatabaseNameResultSetColumnName() != null) {
                        table.setTableSchem(rs.getString(query.getDatabaseNameResultSetColumnName()));
                    }
                    table.setTableName(tableName);
                    table.setRemarks(rs.getString(query.tableComment()));
                    tableList.add(table);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return tableList;
    }

    @Override
    public List<String> getTableTypes() throws RuntimeSQLException {
        return null;
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws RuntimeSQLException {
        return null;
    }

    @Override
    public List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws RuntimeSQLException {
        return null;
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
