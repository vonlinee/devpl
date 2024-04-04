package io.devpl.codegen.db.query;

/**
 * Sqlite 表数据查询
 */
public class SqliteQuery extends AbstractDbQuery {

    @Override
    public String tablesSql() {
        return "select * from sqlite_master where type='table'";
    }

    @Override
    public String tableFieldsSql() {
        return "pragma table_info('%s');";
    }

    @Override
    public String tableName() {
        return "name";
    }

    @Override
    public String tableComment() {
        return "";
    }

    @Override
    public String fieldName() {
        return "name";
    }

    @Override
    public String fieldType() {
        return "type";
    }

    @Override
    public String fieldComment() {
        return "";
    }

    @Override
    public String fieldKey() {
        return "pk";
    }
}
