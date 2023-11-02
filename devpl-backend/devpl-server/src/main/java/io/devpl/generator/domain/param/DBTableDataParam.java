package io.devpl.generator.domain.param;

import lombok.Data;

@Data
public class DBTableDataParam {

    private Integer pageIndex;

    private Integer pageSize;

    private Long dataSourceId;

    private String dbName;

    private String tableName;
}
