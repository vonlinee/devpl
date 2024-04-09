package io.devpl.codegen.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * H2Database 表数据查询
 */
public class H2Query extends AbstractDbQuery {

    public static final String PK_QUERY_SQL = "select * from INFORMATION_SCHEMA.INDEXES WHERE TABLE_NAME = '%s'";

    @Override
    public String tablesSql() {
        return "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE 1=1 ";
    }

    @Override
    public String tableFieldsSql() {
        return "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME= '%s' ";
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "REMARKS";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "TYPE_NAME";
    }

    @Override
    public String fieldComment() {
        return "REMARKS";
    }

    @Override
    public String fieldKey() {
        return "PRIMARY_KEY";
    }

    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return results.getString("SEQUENCE_NAME") != null;
    }
}
