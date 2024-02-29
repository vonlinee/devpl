package io.devpl.backend.domain.param;

import io.devpl.backend.domain.vo.ColumnInfoVO;
import io.devpl.backend.entity.FieldInfo;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class TableCreatorParam {

    @Nullable
    private Long groupId;

    private String tableName;

    private String charset;

    /**
     * 是否使用引号包裹表名和列名
     */
    private Boolean wrapIdentifier = false;
    /**
     * 是否生成DROP Table语句
     */
    private Boolean dropTable = false;

    private List<FieldInfo> fields;

    /**
     * 字段信息
     */
    private List<ColumnInfoVO> columns;
}
