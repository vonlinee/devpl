package com.baomidou.mybatisplus.generator.keywords;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * h2数据库关键字处理
 * <a href="http://www.h2database.com/html/advanced.html#keywords">...</a>
 *
 * @author nieqiurong 2020/5/7.
 * @since 3.3.2
 */
public class H2KeyWordsHandler extends BaseKeyWordsHandler {

    private static final List<String> KEY_WORDS = new ArrayList<>(Arrays.asList(
        "ALL",
        "AND",
        "ARRAY",
        "AS",
        "BETWEEN",
        "BOTH",
        "CASE",
        "CHECK",
        "CONSTRAINT",
        "CROSS",
        "CURRENT_CATALOG",
        "CURRENT_DATE",
        "CURRENT_SCHEMA",
        "CURRENT_TIME",
        "CURRENT_TIMESTAMP",
        "CURRENT_USER",
        "DISTINCT",
        "EXCEPT",
        "EXISTS",
        "FALSE",
        "FETCH",
        "FILTER",
        "FOR",
        "FOREIGN",
        "FROM",
        "FULL",
        "GROUP",
        "GROUPS",
        "HAVING",
        "IF",
        "ILIKE",
        "IN",
        "INNER",
        "INTERSECT",
        "INTERSECTS",
        "INTERVAL",
        "IS",
        "JOIN",
        "LEADING",
        "LEFT",
        "LIKE",
        "LIMIT",
        "LOCALTIME",
        "LOCALTIMESTAMP",
        "MINUS",
        "NATURAL",
        "NOT",
        "NULL",
        "OFFSET",
        "ON",
        "OR",
        "ORDER",
        "OVER",
        "PARTITION",
        "PRIMARY",
        "QUALIFY",
        "RANGE",
        "REGEXP",
        "RIGHT",
        "ROW",
        "_ROWID_",
        "ROWNUM",
        "ROWS",
        "SELECT",
        "SYSDATE",
        "SYSTIME",
        "SYSTIMESTAMP",
        "TABLE",
        "TODAY",
        "TOP",
        "TRAILING",
        "TRUE",
        "UNION",
        "UNIQUE",
        "UNKNOWN",
        "USING",
        "VALUES",
        "WHERE",
        "WINDOW",
        "WITH"
    ));

    public H2KeyWordsHandler() {
        super(new HashSet<>(KEY_WORDS));
    }

    public H2KeyWordsHandler(@NotNull List<String> keyWords) {
        super(new HashSet<>(keyWords));
    }

    public H2KeyWordsHandler(@NotNull Set<String> keyWords) {
        super(keyWords);
    }

    @Override
    public @NotNull String formatStyle() {
        return "\"%s\"";
    }

}
