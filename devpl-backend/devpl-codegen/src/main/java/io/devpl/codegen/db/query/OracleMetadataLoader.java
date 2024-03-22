package io.devpl.codegen.db.query;

import io.devpl.codegen.db.DBType;
import io.devpl.codegen.jdbc.RuntimeSQLException;
import io.devpl.sdk.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Oracle查询
 */
public class OracleMetadataLoader extends AbstractQueryDatabaseMetadataLoader implements SqlMetadataQuery {

    @Override
    public DBType dbType() {
        return DBType.ORACLE;
    }

    @Override
    public String getTableQuerySql(String catalog, String schemaName, String tableName, boolean likeMatch) {
        StringBuilder sql = new StringBuilder();
        sql.append("select dt.table_name, dtc.comments from user_tables dt,user_tab_comments dtc ");
        sql.append("where dt.table_name = dtc.table_name ");
        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append("and dt.table_name = '").append(tableName).append("' ");
        }
        sql.append("order by dt.table_name asc");

        return sql.toString();
    }

    @Override
    public String getTableNameResultSetColumnName() {
        return "table_name";
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
    public String getTableFieldsQuerySql(String catalog, String schema, String tableName, String column, boolean likeMatch) {
        String tableFieldsSql = """
            SELECT A.COLUMN_NAME,
                A.DATA_TYPE,
                B.COMMENTS,
                DECODE(C.POSITION, '1', 'PRI') AS KEY
            FROM ALL_TAB_COLUMNS A
                INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '#schema'
                LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '#schema'
                LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME = A.COLUMN_NAME AND C.OWNER = '#schema'
            WHERE A.OWNER = '#schema' AND A.TABLE_NAME = '%s'
            ORDER BY A.COLUMN_ID
            """;

        Connection connection = getConnection();
        if (connection == null) {
            throw new RuntimeSQLException("cannot get sql to query table fields, cause connection is null");
        }
        try {
            if (isClosed()) {
                DatabaseMetaData md = connection.getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            }
        } catch (SQLException e) {
            throw RuntimeSQLException.wrap(e);
        }
        return tableFieldsSql;
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
