package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceMetadataSyncParam {

    /**
     * 数据源ID
     */
    private Long dataSourceId;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 表名称
     */
    private String tableName;
}
