package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
public class TableImportParam {

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

    private Integer option;

    /**
     * 项目ID
     */
    @Nullable
    private Long projectId;
}
