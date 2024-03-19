package io.devpl.backend.domain.bo;

import lombok.Data;

/**
 * 表导入信息
 */
@Data
public class TableImportInfo {

    /**
     * 数据源 ID
     */
    private Long dataSourceId;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 表名
     */
    private String tableName;
}
