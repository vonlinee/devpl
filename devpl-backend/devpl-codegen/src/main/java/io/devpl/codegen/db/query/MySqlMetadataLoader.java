package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import io.devpl.sdk.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL查询
 */
public class MySqlMetadataLoader extends AbstractQueryDatabaseMetadataLoader implements SqlMetadataQuery {

    @Override
    public DBType dbType() {
        return DBType.MYSQL;
    }

    /**
     * database() 返回当前（默认）数据库的名称：如果连接url指定了数据库，则database()返回该数据库名称
     *
     * @param tableName 表名称
     * @return 表查询sql
     */
    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder("select table_schema, table_name, table_type, table_comment from information_schema.tables ");

        if (schemaName == null || schemaName.isEmpty()) {
            // schemaName为空表示不过滤数据库
            // 如果 database() 返回为NULL，则忽略数据库名称这个查询条件
            sql.append("where 1 = 1 ");
        } else {
            sql.append("where table_schema = '").append(schemaName).append("' ");
        }

        // 表名查询
        if (StringUtils.hasText(tableName)) {
            if (likeMatch) {
                sql.append("and table_name like '%").append(tableName).append("%'");
            } else {
                sql.append("and table_name = '").append(tableName).append("'");
            }
        }
        sql.append(" order by table_schema, table_name asc");

        return sql.toString();
    }

    @Override
    public String getDatabaseNameResultSetColumnName() {
        return "table_schema";
    }

    @Override
    public String getTableNameResultSetColumnName() {
        return "table_name";
    }

    @Override
    public String getTableTypeResultSetColumnName() {
        return "table_type";
    }

    @Override
    public String getTableCommentResultSetColumnName() {
        return "table_comment";
    }

    @Override
    public String getTableCatalogResultSetColumnName() {
        return "table_catalog";
    }

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        String sql = """
            SELECT
            	TABLE_CATALOG,
            	TABLE_SCHEMA,
            	TABLE_NAME,
            	COLUMN_NAME,
            	ORDINAL_POSITION,
            	COLUMN_DEFAULT,
            	IS_NULLABLE,
            	DATA_TYPE,
            	CHARACTER_MAXIMUM_LENGTH,
            	CHARACTER_OCTET_LENGTH,
            	NUMERIC_PRECISION,
            	NUMERIC_SCALE,
            	DATETIME_PRECISION,
            	CHARACTER_SET_NAME,
            	COLLATION_NAME,
            	COLUMN_TYPE,
            	COLUMN_KEY,
            	EXTRA,
            	`PRIVILEGES`,
            	COLUMN_COMMENT,
            	GENERATION_EXPRESSION
            FROM
            	information_schema.`COLUMNS`
            WHERE 1 = 1
            """;

        StringBuilder sqlBuilder = new StringBuilder(sql);
        if (schema != null && !schema.isEmpty()) {
            sqlBuilder.append(" and table_schema = '").append(schema).append("' ");
        }

        if (tableName != null && !tableName.isEmpty()) {
            sqlBuilder.append(" and table_name = '").append(tableName).append("' ");
        }

        sqlBuilder.append(" order by ordinal_position");
        return sqlBuilder.toString();
    }

    @Override
    public List<ColumnMetadata> getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        String sql = getTableFieldQuerySql(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        return queryList(sql, rs -> {
            ColumnMetadata cmd = new ColumnMetadata();
            try {
                cmd.setTableCatalog(rs.getString("TABLE_CATALOG"));
                cmd.setTableSchema(rs.getString("TABLE_SCHEMA"));
                cmd.setTableName(rs.getString("TABLE_NAME"));
                cmd.setColumnName(rs.getString("COLUMN_NAME"));
                cmd.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
                cmd.setColumnDef(rs.getString("COLUMN_DEFAULT"));
                cmd.setNullableDescription(rs.getString("IS_NULLABLE"));
                // dataType
                cmd.setPlatformDataType(rs.getString("DATA_TYPE"));
                // 比如 varchar(255)
                cmd.setDataTypeDescriptor(rs.getString("COLUMN_TYPE"));
                // 字段注释
                cmd.setRemarks(rs.getString("COLUMN_COMMENT"));

                String columnKey = rs.getString("COLUMN_KEY");
                if (columnKey != null && columnKey.contains("PRI")) {
                    cmd.setPrimaryKey(true);
                }
                cmd.setColumnKey(columnKey);
                cmd.setCharsetName(rs.getString("CHARACTER_SET_NAME"));
                cmd.setCollationName(rs.getString("COLLATION_NAME"));

                String extra = rs.getString("EXTRA");
                if (extra != null) {
                    if (extra.contains("auto_increment")) {
                        cmd.setAutoIncrement("YES");
                    }
                }

                cmd.setCharOctetLength(rs.getInt("CHARACTER_OCTET_LENGTH"));

                cmd.addAttribute("characterMaximumLength", rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
                cmd.addAttribute("extra", extra);
                cmd.addAttribute("generationExpression", rs.getString("GENERATION_EXPRESSION"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return cmd;
        });
    }

    @Override
    public String getColumnNameResultSetColumnName() {
        return "column_name";
    }

    @Override
    public String getColumnDataTypeResultSetColumnName() {
        return "data_type";
    }

    @Override
    public String getColumnCommentResultSetColumnName() {
        return "column_comment";
    }

    @Override
    public String getPrimaryKeyResultSetColumnName() {
        return "column_key";
    }

    @Override
    public List<String> getCatalogs() throws SQLException {
        return null;
    }

    @Override
    public List<String> getDatabaseNames() throws SQLException {
        Connection connection = getUsableConnection();
        final List<String> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("show databases")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<String> getDataTypes(String databaseName, String tableName) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT DATA_TYPE FROM information_schema.COLUMNS WHERE 1 = 1 ");

        if (databaseName != null && !databaseName.isEmpty()) {
            sql.append("AND TABLE_SCHEMA = '").append(databaseName).append("' ");
        }
        if (tableName != null && !tableName.isEmpty()) {
            sql.append("AND TABLE_NAME = '").append(tableName).append("' ");
        }
        sql.append(" ORDER BY DATA_TYPE");

        return query(sql, resultSet -> {
            List<String> dataTypes = new ArrayList<>();
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    dataTypes.add(resultSet.getString(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return dataTypes;
        });
    }
}
