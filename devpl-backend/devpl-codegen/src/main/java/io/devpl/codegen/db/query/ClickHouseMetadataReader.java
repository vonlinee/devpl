package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.sdk.util.StringUtils;

import java.util.List;

/**
 * ClickHouse 表数据查询
 */
public class ClickHouseMetadataReader extends AbstractQueryDatabaseMetadataReader implements SqlMetadataQuery {

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        return "select * from system.columns where table='%s'";
    }

    @Override
    public DBType dbType() {
        return DBType.CLICK_HOUSE;
    }

    /**
     * <a href="https://clickhouse.com/docs/zh/operations/system-tables/tables">...</a>
     *
     * @param tableName 表名
     * @param likeMatch 是否模糊匹配
     * @return 表查询sql
     */
    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM system.tables WHERE 1=1 ");

        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and name = '").append(tableName).append("' ");
        }
        return sql.toString();
    }

    @Override
    public String getTableNameResultSetColumnName() {
        return "name";
    }

    @Override
    public String getDatabaseNameResultSetColumnName() {
        return null;
    }

    @Override
    public String getTableCommentResultSetColumnName() {
        return "comment";
    }

    @Override
    public String getTableCatalogResultSetColumnName() {
        return null;
    }

    @Override
    public String getColumnNameResultSetColumnName() {
        return "name";
    }


    @Override
    public String getColumnDataTypeResultSetColumnName() {
        return "type";
    }


    @Override
    public String getColumnCommentResultSetColumnName() {
        return "comment";
    }


    @Override
    public String getPrimaryKeyResultSetColumnName() {
        return "is_in_primary_key";
    }

    @Override
    public List<String> getDatabaseNames() {
        return null;
    }
}
