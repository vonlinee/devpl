package io.devpl.codegen.db;

import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import lombok.Data;

import java.util.Map;

/**
 * 列信息
 */
@Data
public class ColumnInfo {

    private String name;
    private String fullName;
    private String comment;
    private String tableName;
    private String dataType;
    private String dataTypeDefinition;
    private String charsetDefinition;
    private ColumnMetadata metadata;
    private Map<String, Object> attributes;
}
