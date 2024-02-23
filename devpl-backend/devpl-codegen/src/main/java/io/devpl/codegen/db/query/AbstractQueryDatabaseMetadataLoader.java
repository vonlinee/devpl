package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.codegen.jdbc.meta.*;
import io.devpl.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 重构
 * 通过不同平台的各自的sql进行查询
 *
 * @see io.devpl.codegen.jdbc.JdbcDatabaseMetadataLoader
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

    /**
     * 获取表信息获取sql
     *
     * @param tableNamePattern 表名
     * @return sql, 用于获取表信息
     */
    String getTableFieldQuerySql(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) {
        String tableFieldsSql = query.getTableFieldsQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern, true);
        try {
            if (dbType == DBType.ORACLE) {
                DatabaseMetaData md = connection.getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableNamePattern);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableNamePattern);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tableFieldsSql;
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {

        List<ColumnMetadata> columnsMetadata = new ArrayList<>();

        String tableFieldsSql = getTableFieldQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern);

        try (PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    ColumnMetadata cmd = new ColumnMetadata();
                    cmd.setColumnName(rs.getString(query.fieldName()));
                    String fieldType = rs.getString(query.fieldType());
                    if (fieldType.contains(" ")) {
                        fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                    }

                    cmd.setPlatformDataType(fieldType);
                    cmd.setRemarks(rs.getString(query.fieldComment()));

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
                    String key = rs.getString(query.fieldKey());
                    if (StringUtils.hasText(key) && "PRI".equalsIgnoreCase(key)) {
                        PrimaryKeyMetadata pkm = new PrimaryKeyMetadata();
                        pkm.setColumnName(rs.getString(query.fieldName()));
                        primaryKeyMetadata.add(pkm);
                    }
                }
            }
        }
        return primaryKeyMetadata;
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
