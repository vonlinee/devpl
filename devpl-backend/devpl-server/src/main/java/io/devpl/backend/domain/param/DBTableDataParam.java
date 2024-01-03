package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DbConnInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBTableDataParam {

    private Integer pageIndex;

    private Integer pageSize;

    /**
     * 数据源ID
     */
    private Long dataSourceId;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 表格名称
     */
    private String tableName;

    /**
     * 连接配置
     */
    private DbConnInfo connInfo;
}
