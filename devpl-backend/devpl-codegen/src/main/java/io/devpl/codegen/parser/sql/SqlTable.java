package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * sql 中涉及到的表信息
 */
@Setter
@Getter
public class SqlTable {

    protected String name;

    protected String catalog;

    protected String schema;
}
