package io.devpl.codegen.db.querys;

import io.devpl.sdk.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PostgresSql 表数据查询
 */
public class PostgresSqlQuery extends AbstractDbQuery {

    @Override
    public String tablesSql() {
        return "SELECT A.tablename, obj_description(relfilenode, 'pg_class') AS comments FROM pg_tables A, pg_class B WHERE A.schemaname='%s' AND A.tablename = B.relname";
    }

    @Override
    public String tableFieldsSql() {
        return """
            SELECT
               A.attname AS name,format_type (A.atttypid,A.atttypmod) AS type,col_description (A.attrelid,A.attnum) AS comment,
            \t D.column_default,
               CASE WHEN length(B.attname) > 0 THEN 'PRI' ELSE '' END AS key
            FROM
               pg_attribute A
            LEFT JOIN (
                SELECT
                    pg_attribute.attname
                FROM
                    pg_index,
                    pg_class,
                    pg_attribute
                WHERE
                    pg_class.oid ='"%s"' :: regclass
                AND pg_index.indrelid = pg_class.oid
                AND pg_attribute.attrelid = pg_class.oid
                AND pg_attribute.attnum = ANY (pg_index.indkey)
            ) B ON A.attname = b.attname
            INNER JOIN pg_class C on A.attrelid = C.oid
            INNER JOIN information_schema.columns D on A.attname = D.column_name
            WHERE A.attrelid ='"%s"' :: regclass AND A.attnum> 0 AND NOT A.attisdropped AND D.table_name = '%s'
            ORDER BY A.attnum;""";
    }

    @Override
    public String tableName() {
        return "tablename";
    }

    @Override
    public String tableComment() {
        return "comments";
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
        return "comment";
    }

    @Override
    public String fieldKey() {
        return "key";
    }

    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return StringUtils.hasText(results.getString("column_default")) && results.getString("column_default")
            .contains("nextval");
    }
}
