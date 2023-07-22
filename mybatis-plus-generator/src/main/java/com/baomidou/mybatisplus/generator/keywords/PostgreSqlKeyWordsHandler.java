package com.baomidou.mybatisplus.generator.keywords;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * postgresql关键字处理
 * <a href="https://www.postgresql.org/docs/11/sql-keywords-appendix.html">...</a>
 * @author nieqiurong 2020/5/9.
 * @since 3.3.2
 */
public class PostgreSqlKeyWordsHandler extends BaseKeyWordsHandler {

    private static final List<String> KEY_WORDS = new ArrayList<>(Arrays.asList(
        "ALL",
        "ANALYSE",
        "ANALYZE",
        "AND",
        "ANY",
        "ARRAY",
        "AS",
        "ASC",
        "ASYMMETRIC",
        "AUTHORIZATION",
        "BINARY",
        "BOTH",
        "CASE",
        "CAST",
        "CHECK",
        "COLLATE",
        "COLLATION",
        "COLUMN",
        "CONCURRENTLY",
        "CONSTRAINT",
        "CREATE",
        "CROSS",
        "CURRENT_CATALOG",
        "CURRENT_DATE",
        "CURRENT_ROLE",
        "CURRENT_SCHEMA",
        "CURRENT_TIME",
        "CURRENT_TIMESTAMP",
        "CURRENT_USER",
        "DEFAULT",
        "DEFERRABLE",
        "DESC",
        "DISTINCT",
        "DO",
        "ELSE",
        "END",
        "EXCEPT",
        "FALSE",
        "FETCH",
        "FOR",
        "FOREIGN",
        "FREEZE",
        "FROM",
        "FULL",
        "GRANT",
        "GROUP",
        "HAVING",
        "ILIKE",
        "IN",
        "INITIALLY",
        "INNER",
        "INTERSECT",
        "INTO",
        "IS",
        "ISNULL",
        "JOIN",
        "LATERAL",
        "LEADING",
        "LEFT",
        "LIKE",
        "LIMIT",
        "LOCALTIME",
        "LOCALTIMESTAMP",
        "NATURAL",
        "NOT",
        "NOTNULL",
        "NULL",
        "OFFSET",
        "ON",
        "ONLY",
        "OR",
        "ORDER",
        "OUTER",
        "OVERLAPS",
        "PLACING",
        "PRIMARY",
        "REFERENCES",
        "RETURNING",
        "RIGHT",
        "SELECT",
        "SESSION_USER",
        "SIMILAR",
        "SOME",
        "SYMMETRIC",
        "TABLE",
        "TABLESAMPLE",
        "THEN",
        "TO",
        "TRAILING",
        "TRUE",
        "UNION",
        "UNIQUE",
        "USER",
        "USING",
        "VARIADIC",
        "VERBOSE",
        "WHEN",
        "WHERE",
        "WINDOW",
        "WITH"
    ));

    public PostgreSqlKeyWordsHandler() {
        super(new HashSet<>(KEY_WORDS));
    }

    public PostgreSqlKeyWordsHandler(@NotNull List<String> keyWords) {
        super(new HashSet<>(keyWords));
    }

    public PostgreSqlKeyWordsHandler(@NotNull Set<String> keyWords) {
        super(keyWords);
    }

    @Override
    public @NotNull String formatStyle() {
        return "\"%s\"";
    }

}
