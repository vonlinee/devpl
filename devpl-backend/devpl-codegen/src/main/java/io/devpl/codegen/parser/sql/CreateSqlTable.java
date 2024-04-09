package io.devpl.codegen.parser.sql;

import io.devpl.codegen.db.IndexInfo;
import io.devpl.codegen.jdbc.meta.TableMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * ddl sql相关的表信息
 */
@Getter
@Setter
public class CreateSqlTable extends SqlTable {

    private String name;
    private String comment;

    private TableMetadata metadata;
    private List<CreateSqlColumn> columns;

    private List<IndexInfo> indexes;
    private List<Map.Entry<String, String>> options;
}
