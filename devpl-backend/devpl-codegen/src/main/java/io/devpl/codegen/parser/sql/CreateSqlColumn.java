package io.devpl.codegen.parser.sql;

import lombok.Getter;
import lombok.Setter;
import org.apache.ddlutils.jdbc.meta.ColumnMetadata;

import java.util.Map;

/**
 * 列信息
 */
@Getter
@Setter
public class CreateSqlColumn extends SqlColumn {

    private String name;
    private String fullName;
    private String comment;
    private String tableName;
    private String dataType;
    private String dataTypeDefinition;
    private String charsetDefinition;
    private String defaultExpression;
    private boolean hasDefaultExpression;
    private ColumnMetadata metadata;
    private Map<String, Object> attributes;
}
