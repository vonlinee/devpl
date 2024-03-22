package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.ConnectionHolder;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.*;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过不同平台的各自的sql进行查询
 *
 * @see AbstractQuery
 */
@Slf4j
public class AbstractQueryDatabaseMetadataLoader implements DatabaseMetadataLoader {

    AbstractQuery query;
    DBType dbType;
    Connection connection;

    public AbstractQueryDatabaseMetadataLoader(Connection connection, DBType dbType) {
        this.connection = connection;
        this.dbType = dbType;
        this.query = getQuery(dbType);
    }

    private AbstractQuery getQuery(DBType dbType) {
        AbstractQuery dbQuery = null;
        if (dbType == DBType.MYSQL) {
            dbQuery = new MySqlQuery();
        } else if (dbType == DBType.ORACLE) {
            dbQuery = new OracleQuery();
        } else if (dbType == DBType.POSTGRE_SQL) {
            dbQuery = new PostgreSqlQuery();
        } else if (dbType == DBType.SQL_SERVER) {
            dbQuery = new SQLServerQuery();
        } else if (dbType == DBType.DM) {
            dbQuery = new DmQuery();
        } else if (dbType == DBType.CLICK_HOUSE) {
            dbQuery = new ClickHouseQuery();
        }

        if (dbQuery != null) {
            dbQuery.setConnection(connection);
            ConnectionHolder ch = (ConnectionHolder) dbQuery;
            ch.setConnectionSupplier(() -> connection);
        }

        return dbQuery;
    }

    @Override
    public boolean setConnection(Connection connection) {
        this.connection = connection;
        return true;
    }

    @Override
    public List<String> getCatalogs() {
        return null;
    }

    @Override
    public List<String> getDatabaseNames() throws SQLException {
        return query.getDatabaseNames();
    }

    @Override
    public List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws RuntimeSQLException {
        List<TableMetadata> tableList = new ArrayList<>();

        String tableQuerySql = query.getTableQuerySql(catalog, schemaPattern, tableNamePattern, true);

        if (log.isDebugEnabled()) {
            log.debug("search catalog: {}, schema: {}, tableName: {}, retrieve metadata of tables with sql: {}", catalog, schemaPattern, tableNamePattern, tableQuerySql);
        }

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
                        table.setTableSchema(rs.getString(query.getDatabaseNameResultSetColumnName()));
                    }
                    table.setTableName(tableName);
                    if (query.getTableTypeResultSetColumnName() != null) {
                        table.setTableType(rs.getString(query.getTableTypeResultSetColumnName()));
                    }
                    table.setRemarks(rs.getString(query.getTableCommentResultSetColumnName()));
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
        throw new UnsupportedOperationException();
    }

    /**
     * 获取表信息获取sql
     *
     * @param tableNamePattern 表名
     * @return sql, 用于获取表信息
     */
    String getTableFieldQuerySql(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        return query.getTableFieldsQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern, true);
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {

        List<ColumnMetadata> columnsMetadata = new ArrayList<>();

        String tableFieldsSql = getTableFieldQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern);

        try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ColumnMetadata cmd = new ColumnMetadata();
                    cmd.setColumnName(rs.getString(query.getColumnNameResultSetColumnName()));
                    String fieldType = rs.getString(query.getColumnDataTypeResultSetColumnName());
                    if (fieldType.contains(" ")) {
                        fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                    }

                    cmd.setPlatformDataType(fieldType);
                    cmd.setRemarks(rs.getString(query.getColumnCommentResultSetColumnName()));
                    cmd.setTableSchema(rs.getString(query.getDatabaseNameResultSetColumnName()));
                    cmd.setTableName(rs.getString(query.getTableNameResultSetColumnName()));
                    cmd.setTableCatalog(rs.getString(query.getTableCatalogResultSetColumnName()));
                    // 主键
                    // String key = rs.getString(query.fieldKey());
                    // field.setPrimaryKey(StringUtils.hasText(key) && "PRI".equalsIgnoreCase(key));

                    columnsMetadata.add(cmd);
                }
            }
        }
        return columnsMetadata;
    }

    @Override
    public List<PrimaryKeyMetadata> getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        String tableFieldsSql = getTableFieldQuerySql(catalog, schema, table, null);
        List<PrimaryKeyMetadata> primaryKeyMetadata = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    // 主键
                    String key = rs.getString(query.getPrimaryKeyResultSetColumnName());
                    if (StringUtils.hasText(key) && "PRI".equalsIgnoreCase(key)) {
                        PrimaryKeyMetadata pkm = new PrimaryKeyMetadata();
                        pkm.setColumnName(rs.getString(query.getColumnNameResultSetColumnName()));
                        primaryKeyMetadata.add(pkm);
                    }
                }
            }
        }
        return primaryKeyMetadata;
    }

    @Override
    public List<FunctionMetadata> getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws RuntimeSQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getSQLKeywords() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDataTypes(String databaseName, String tableName) throws SQLException {
        return query.getDataTypes(databaseName, tableName);
    }

    @Override
    public void close() {
        JdbcUtils.closeQuietly(this.connection);
    }
}
