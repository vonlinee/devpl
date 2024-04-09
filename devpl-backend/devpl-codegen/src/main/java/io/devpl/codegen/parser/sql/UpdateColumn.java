package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateColumn extends SqlColumn {

    private String value;
}
