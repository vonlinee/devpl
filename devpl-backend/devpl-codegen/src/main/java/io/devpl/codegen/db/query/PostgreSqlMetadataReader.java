package io.devpl.codegen.db.query;

import io.devpl.sdk.util.StringUtils;
import org.apache.ddlutils.platform.BuiltinDatabaseType;

import java.sql.SQLException;
import java.util.List;

/**
 * PostgreSql查询
 */
public class PostgreSqlMetadataReader extends AbstractQueryDatabaseMetadataReader implements SqlMetadataQuery {

    @Override
    public BuiltinDatabaseType dbType() {
        return BuiltinDatabaseType.POSTGRE_SQL;
    }

    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t1.tablename, obj_description(relfilenode, 'pg_class') as comments from pg_tables t1, pg_class t2 ");
        sql.append("where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname ");
        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and t1.tablename = '").append(tableName).append("' ");
        }

        return sql.toString();
    }

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        return "select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as columnComment,"
               + "(CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey "
               + "from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid "
               + "left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid "
               + "where t1.relname = '%s' and t2.attrelid = t1.oid and t2.attnum>0";
    }


    @Override
    public String getTableNameResultSetColumnName() {
        return "tablename";
    }

    @Override
    public String getDatabaseNameResultSetColumnName() {
        return null;
    }

    @Override
    public String getTableCommentResultSetColumnName() {
        return "comments";
    }

    @Override
    public String getTableCatalogResultSetColumnName() {
        return null;
    }

    @Override
    public String getColumnNameResultSetColumnName() {
        return "columnName";
    }

    @Override
    public String getColumnDataTypeResultSetColumnName() {
        return "dataType";
    }

    @Override
    public String getColumnCommentResultSetColumnName() {
        return "columnComment";
    }

    @Override
    public String getPrimaryKeyResultSetColumnName() {
        return "columnKey";
    }

    @Override
    public List<String> getDatabaseNames() throws SQLException {
        return null;
    }
}
