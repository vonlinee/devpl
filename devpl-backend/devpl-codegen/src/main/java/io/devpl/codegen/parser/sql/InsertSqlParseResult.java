package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsertSqlParseResult {

    private SqlTable table;

    private List<InsertColumn> insertColumns;

    /**
     * 插入字段的值
     */
    private List<List<String>> columnValues;
}
