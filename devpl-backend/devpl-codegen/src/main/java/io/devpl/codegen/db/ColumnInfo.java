package io.devpl.codegen.db;

import io.devpl.codegen.jdbc.meta.ColumnMetadata;
import lombok.Data;

/**
 * 列信息
 */
@Data
public class ColumnInfo {

    ColumnMetadata metadata;
}
