package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * sql中的列信息
 */
@Setter
@Getter
public class SqlColumn {

    protected String tableName;
    protected String columnName;
}
