package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBTypeEnum;
import io.devpl.codegen.jdbc.ConnectionHolder;
import io.devpl.codegen.jdbc.JdbcUtils;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 通过不同平台的各自的sql进行查询
 *
 * @see ConnectionHolder
 * @see SqlMetadataQuery
 */
@Slf4j
public abstract class AbstractQueryDatabaseMetadataReader extends ConnectionHolder implements DatabaseMetadataReader, SqlMetadataQuery {

    public static DatabaseMetadataReader getQuery(DBTypeEnum dbType) {
        DatabaseMetadataReader dbQuery = null;
        if (dbType == DBTypeEnum.MYSQL) {
            dbQuery = new MySqlMetadataReader();
        } else if (dbType == DBTypeEnum.ORACLE) {
            dbQuery = new OracleMetadataReader();
        } else if (dbType == DBTypeEnum.POSTGRE_SQL) {
            dbQuery = new PostgreSqlMetadataReader();
        } else if (dbType == DBTypeEnum.SQL_SERVER) {
            dbQuery = new SQLServerMetadataReader();
        } else if (dbType == DBTypeEnum.DM) {
            dbQuery = new DmMetadataReader();
        } else if (dbType == DBTypeEnum.CLICK_HOUSE) {
            dbQuery = new ClickHouseMetadataReader();
        }
        return dbQuery;
    }

    /**
     * 查询列表
     *
     * @param sql      sql
     * @param consumer 结果处理
     * @param <R>      单行数据类型
     * @return 数据集
     * @throws SQLException 执行出错
     */
    final <R> R query(CharSequence sql, Function<ResultSet, R> consumer) throws SQLException {
        Connection connection = getUsableConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return consumer.apply(resultSet);
            }
        }
    }

    /**
     * 查询列表
     *
     * @param sql      sql
     * @param consumer 结果处理
     * @param <R>      单行数据类型
     * @return 数据集
     * @throws SQLException 执行出错
     */
    final <R> List<R> queryList(CharSequence sql, Function<ResultSet, R> consumer) throws SQLException {
        Connection connection = getUsableConnection();
        List<R> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(consumer.apply(resultSet));
                }
            }
        }
        return list;
    }

    final <R> R query(CharSequence sql, BiFunction<Connection, ResultSet, R> consumer) throws SQLException {
        Connection connection = getUsableConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql))) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return consumer.apply(connection, resultSet);
            }
        }
    }

    @Override
    public List<String> getCatalogs() throws SQLException {
        return null;
    }

    @Override
    public List<TableMetadata> getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        List<TableMetadata> tableList = new ArrayList<>();

        String tableQuerySql = this.getTableQuerySql(catalog, schemaPattern, tableNamePattern, true);

        if (log.isDebugEnabled()) {
            log.debug("search catalog: {}, schema: {}, tableName: {}, retrieve metadata of tables with sql: {}", catalog, schemaPattern, tableNamePattern, tableQuerySql);
        }

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(tableQuerySql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                // 查询数据
                while (rs.next()) {
                    String tableName = rs.getString(this.getTableNameResultSetColumnName());
                    if (tableNamePattern != null && !tableName.contains(tableNamePattern)) {
                        continue;
                    }
                    TableMetadata table = new TableMetadata();
                    if (this.getDatabaseNameResultSetColumnName() != null) {
                        table.setTableSchema(rs.getString(this.getDatabaseNameResultSetColumnName()));
                    }
                    table.setTableName(tableName);
                    if (this.getTableTypeResultSetColumnName() != null) {
                        table.setTableType(rs.getString(this.getTableTypeResultSetColumnName()));
                    }
                    table.setRemarks(rs.getString(this.getTableCommentResultSetColumnName()));
                    tableList.add(table);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
        return tableList;
    }

    @Override
    public List<String> getTableTypes() throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取表信息获取sql
     *
     * @param tableNamePattern 表名
     * @return sql, 用于获取表信息
     */
    String getTableFieldQuerySql(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        return this.getTableFieldsQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern, true);
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        List<ColumnMetadata> columnsMetadata = new ArrayList<>();
        String tableFieldsSql = getTableFieldQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(tableFieldsSql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ColumnMetadata cmd = new ColumnMetadata();
                    cmd.setColumnName(rs.getString(this.getColumnNameResultSetColumnName()));
                    String fieldType = rs.getString(this.getColumnDataTypeResultSetColumnName());
                    if (fieldType.contains(" ")) {
                        fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                    }

                    cmd.setPlatformDataType(fieldType);
                    cmd.setRemarks(rs.getString(this.getColumnCommentResultSetColumnName()));
                    cmd.setTableSchema(rs.getString(this.getDatabaseNameResultSetColumnName()));
                    cmd.setTableName(rs.getString(this.getTableNameResultSetColumnName()));
                    cmd.setTableCatalog(rs.getString(this.getTableCatalogResultSetColumnName()));
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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(tableFieldsSql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    // 主键
                    String key = rs.getString(this.getPrimaryKeyResultSetColumnName());
                    if (key != null && !key.isEmpty() && "PRI".equalsIgnoreCase(key)) {
                        PrimaryKeyMetadata pkm = new PrimaryKeyMetadata();
                        pkm.setColumnName(rs.getString(this.getColumnNameResultSetColumnName()));
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
    public List<ColumnPrivilegesMetadata> getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getSQLKeywords() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDataTypes(String databaseName, String tableName) throws SQLException {
        return Collections.emptyList();
    }

    public void close() {
        JdbcUtils.closeQuietly(this.getConnection());
    }
}
