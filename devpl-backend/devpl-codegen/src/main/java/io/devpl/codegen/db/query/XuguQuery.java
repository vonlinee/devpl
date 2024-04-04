package io.devpl.codegen.db.query;

/**
 * Xugu 表数据查询
 */
public class XuguQuery extends AbstractDbQuery {

    @Override
    public String tablesSql() {
        return "SELECT * FROM ALL_TABLES WHERE 1 = 1";
    }

    @Override
    public String tableFieldsSql() {
        return "SELECT B.COL_NAME,B.TYPE_NAME,B.COMMENTS, '' AS KEY FROM ALL_TABLES A INNER JOIN ALL_COLUMNS B ON A.TABLE_ID = B.TABLE_ID WHERE A.TABLE_NAME = '%s'";
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldName() {
        return "COL_NAME";
    }

    @Override
    public String fieldType() {
        return "TYPE_NAME";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }
}
