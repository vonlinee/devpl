package io.devpl.backend.domain.param;

import io.devpl.backend.domain.vo.ColumnInfoVO;
import io.devpl.backend.entity.FieldInfo;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 字段信息转成表信息参数
 */
@Data
public class FieldsToTableParam {

    /**
     * 字段组ID
     */
    @Nullable
    private Long groupId;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表编码
     */
    private String charset;

    /**
     * 数据库类型
     *
     * @see BuiltinDatabaseType
     */
    private String dbType;

    /**
     * 是否使用引号包裹表名和列名
     */
    private Boolean wrapIdentifier = false;
    /**
     * 是否生成DROP Table语句
     */
    private Boolean dropTable = false;

    /**
     * 字段列表
     */
    private List<FieldInfo> fields;

    /**
     * 字段信息
     */
    private List<ColumnInfoVO> columns;
}
