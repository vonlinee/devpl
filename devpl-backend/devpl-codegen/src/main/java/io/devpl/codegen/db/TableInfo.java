package io.devpl.codegen.db;

import io.devpl.codegen.jdbc.meta.TableMetadata;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 表信息
 */
@Data
public class TableInfo {

    private String name;
    private String comment;

    private TableMetadata metadata;
    private List<ColumnInfo> columns;

    private List<IndexInfo> indexes;
    private List<Map.Entry<String, String>> options;
}
