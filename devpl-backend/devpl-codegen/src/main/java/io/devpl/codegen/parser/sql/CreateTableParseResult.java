package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTableParseResult {

    /**
     * 表信息
     */
    private CreateSqlTable createSqlTable;
}
