package io.devpl.codegen.db;

import io.devpl.codegen.jdbc.meta.TableMetadata;
import lombok.Data;

/**
 * 表信息
 */
@Data
public class TableInfo {

    TableMetadata metadata;
}
