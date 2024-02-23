package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.sdk.util.StringUtils;

/**
 * MySQL查询
 */
public class MySqlQuery extends AbstractQueryBase implements AbstractQuery {

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
        StringBuilder sql = new StringBuilder("select table_schema, table_name, table_comment from information_schema.tables ");

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
    public String getTableCommentResultSetColumnName() {
        return "table_comment";
    }

    @Override
    public String getTableCatalogResultSetColumnName() {
        return "table_catalog";
    }

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        StringBuilder sql = new StringBuilder("select table_catalog, table_schema, table_name, column_name, data_type, column_comment, column_key from information_schema.columns where 1 = 1");

        if (schema != null && !schema.isEmpty()) {
            sql.append(" and table_schema = '").append(schema).append("' ");
        }

        if (tableName != null && !tableName.isEmpty()) {
            sql.append(" and table_name = '").append(tableName).append("' ");
        }

        sql.append(" order by ordinal_position");

        return sql.toString();
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
}
