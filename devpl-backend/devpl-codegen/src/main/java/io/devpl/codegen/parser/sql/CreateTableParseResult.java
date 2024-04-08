package io.devpl.codegen.parser.sql;

import io.devpl.codegen.db.TableInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTableParseResult {

    TableInfo tableInfo;
}
