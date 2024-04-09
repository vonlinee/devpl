package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsertSqlParseResult {

    /**
     * 插入语句涉及到的表
     */
    private SqlTable table;

    /**
     * 插入的列
     */
    private List<InsertColumn> insertColumns;

    /**
     * 插入字段的值
     */
    private List<List<String>> columnValues;
}
