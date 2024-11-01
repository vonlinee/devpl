package io.devpl.codegen.db.query;

import io.devpl.sdk.util.StringUtils;
import org.apache.ddlutils.platform.BuiltinDatabaseType;

import java.sql.SQLException;
import java.util.List;

/**
 * SQLServer查询
 */
public class SQLServerMetadataReader extends AbstractQueryDatabaseMetadataReader implements SqlMetadataQuery {

    @Override
    public BuiltinDatabaseType dbType() {
        return BuiltinDatabaseType.SQL_SERVER;
    }

    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cast(so.name as varchar(500)) as TABLE_NAME, cast(sep.value as varchar(500)) as COMMENTS from sysobjects so ");
        sql.append("left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0 where (xtype='U' or xtype='V') ");

        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and cast(so.name as varchar(500)) = '").append(tableName).append("' ");
        }
        sql.append(" order by cast(so.name as varchar(500))");

        return sql.toString();
    }

    @Override
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        return """
                SELECT
                    cast(a.name AS VARCHAR(500)) AS TABLE_NAME,
                    cast(b.name AS VARCHAR(500)) AS COLUMN_NAME,
                    cast(c.VALUE AS NVARCHAR(500)) AS COMMENTS,
                    cast(sys.types.name AS VARCHAR(500)) AS DATA_TYPE,
                    (
                        SELECT
                            CASE
                                WHEN count(1) = 1 THEN 'PRI'
                                ELSE ''
                            END
                        FROM
                            syscolumns, sysobjects, sysindexes, sysindexkeys, systypes
                        WHERE
                            syscolumns.xusertype = systypes.xusertype
                            AND syscolumns.id = object_id(a.name)
                            AND sysobjects.xtype = 'PK'
                            AND sysobjects.parent_obj = syscolumns.id
                            AND sysindexes.id = syscolumns.id
                            AND sysobjects.name = sysindexes.name
                            AND sysindexkeys.id = syscolumns.id
                            AND sysindexkeys.indid = sysindexes.indid
                            AND syscolumns.colid = sysindexkeys.colid
                            AND syscolumns.name = b.name
                    ) as 'KEY',
                    b.is_identity isIdentity
                FROM
                    (
                        SELECT name, object_id FROM sys.tables
                        UNION ALL
                        SELECT name, object_id FROM sys.views
                    ) a
                INNER JOIN sys.columns b ON b.object_id = a.object_id
                LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id
                LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id
                WHERE
                    a.name = '%s'
                    AND sys.types.name != 'sysname'
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
        return "COMMENTS";
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
    public List<String> getDatabaseNames() throws SQLException {
        return null;
    }
}
