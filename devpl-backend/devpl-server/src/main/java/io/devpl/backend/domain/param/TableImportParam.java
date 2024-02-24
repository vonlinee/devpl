package io.devpl.backend.domain.param;

import io.devpl.backend.entity.RdbmsConnectionInfo;
import io.devpl.codegen.db.DBType;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "数据源为空")
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
     * 项目ID，如果选择了项目，则使用项目的路径信息
     */
    @Nullable
    private Long projectId;

    private DBType dbType;

    /**
     * 后端填充
     */
    private RdbmsConnectionInfo connInfo;
}
