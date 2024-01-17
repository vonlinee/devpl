package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
public class TableImportParam {

    /**
     * 数据源ID
     */
    @Nullable
    private Long dataSourceId;

    /**
     * 单个导入时使用
     */
    @Nullable
    private String tableName;

    /**
     * 批量导入时使用
     */
    @Nullable
    private List<String> tableNameList;

    /**
     * 导入选项
     */
    private Integer option;

    /**
     * 项目ID
     */
    @Nullable
    private Long projectId;
}
