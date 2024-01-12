package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 字段Key，区分大小写
     */
    @TableField(value = "field_key")
    private String fieldKey;

    /**
     * 类型分组ID
     */
    @TableField(value = "type_group_id")
    private String typeGroupId;

    /**
     * 字段名，中文名
     */
    private String fieldName;

    /**
     * 数据类型
     *
     * @see DataTypeItem#getTypeKey()
     */
    private String dataType;

    /**
     * 字段注释信息，相比description字段比较简短
     *
     * @see FieldInfo#description
     */
    private String comment;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 默认值
     */
    @TableField(value = "field_value")
    private String defaultValue;

    public String getFieldName() {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldKey;
        }
        return fieldName;
    }
}
