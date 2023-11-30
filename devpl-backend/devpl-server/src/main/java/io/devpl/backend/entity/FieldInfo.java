package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 字段信息表
 */
@Getter
@Setter
@TableName(value = "field_info")
public class FieldInfo extends DBEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字段Key
     */
    @TableField(value = "field_key", insertStrategy = FieldStrategy.IGNORED)
    private String fieldKey;

    /**
     * 字段名，中文名
     */
    private String fieldName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 默认值
     */
    @TableField(value = "field_value")
    private String defaultValue;
}
