package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBTypeEnum;
import io.devpl.sdk.util.StringUtils;

import java.util.List;

/**
 * 达梦8 查询
 */
public class DmMetadataReader extends AbstractQueryDatabaseMetadataReader implements SqlMetadataQuery {

    @Override
    public DBTypeEnum dbType() {
        return DBTypeEnum.MYSQL;
    }

    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T.* FROM (SELECT DISTINCT T1.TABLE_NAME AS TABLE_NAME,T2.COMMENTS AS TABLE_COMMENT FROM USER_TAB_COLUMNS T1 ");
        sql.append("INNER JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME = T2.TABLE_NAME) T WHERE 1=1 ");
        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and T.TABLE_NAME = '").append(tableName).append("' ");
        }
        sql.append("order by T.TABLE_NAME asc");
        return sql.toString();
    }

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String columnName, boolean likeMatch) {
        return """
            SELECT T2.COLUMN_NAME,T1.COMMENTS,
            CASE WHEN T2.DATA_TYPE='NUMBER' THEN (CASE WHEN T2.DATA_PRECISION IS NULL THEN T2.DATA_TYPE WHEN NVL(T2.DATA_SCALE, 0) > 0 THEN T2.DATA_TYPE||'('||T2.DATA_PRECISION||','||T2.DATA_SCALE||')' ELSE T2.DATA_TYPE||'('||T2.DATA_PRECISION||')' END) ELSE T2.DATA_TYPE END DATA_TYPE ,
            CASE WHEN CONSTRAINT_TYPE='P' THEN 'PRI' END AS KEY
            FROM USER_COL_COMMENTS T1, USER_TAB_COLUMNS T2,
            (SELECT T4.TABLE_NAME, T4.COLUMN_NAME ,T5.CONSTRAINT_TYPE
            FROM USER_CONS_COLUMNS T4, USER_CONSTRAINTS T5
            WHERE T4.CONSTRAINT_NAME = T5.CONSTRAINT_NAME
            AND T5.CONSTRAINT_TYPE = 'P')T3
            WHERE T1.TABLE_NAME = T2.TABLE_NAME AND
            T1.COLUMN_NAME=T2.COLUMN_NAME AND
            T1.TABLE_NAME = T3.TABLE_NAME(+) AND
            T1.COLUMN_NAME=T3.COLUMN_NAME(+)   AND
            T1.TABLE_NAME = '%s'
            ORDER BY T2.TABLE_NAME,T2.COLUMN_ID
            """;
    }

    @Override
    public String getTableNameResultSetColumnName() {
        return "TABLE_NAME";
    }

    @Override
    public String getDatabaseNameResultSetColumnName() {
        return null;
    }

    @Override
    public String getTableCommentResultSetColumnName() {
        return "TABLE_COMMENT";
    }

    @Override
    public String getTableCatalogResultSetColumnName() {
        return null;
    }

    @Override
    public String getColumnNameResultSetColumnName() {
        return "COLUMN_NAME";
    }

    @Override
    public String getColumnDataTypeResultSetColumnName() {
        return "DATA_TYPE";
    }

    @Override
    public String getColumnCommentResultSetColumnName() {
        return "COMMENTS";
    }

    @Override
    public String getPrimaryKeyResultSetColumnName() {
        return "KEY";
    }

    @Override
    public List<String> getDatabaseNames() {
        return null;
    }
}
