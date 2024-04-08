package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateColumn {

    private String tableName;
    private String columnName;
    private String value;
}
