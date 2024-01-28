package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

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
     * 父级字段主键ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

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

    /**
     * 是否临时字段，可被删除
     */
    @TableField(value = "temporary")
    private Boolean temporary;

    /**
     * 是否可选字段
     */
    @TableField(value = "optional")
    private Boolean optional;

    /**
     * 嵌套的字段列表
     */
    @TableField(exist = false)
    private List<FieldInfo> children;

    public String getFieldName() {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldKey;
        }
        return fieldName;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public List<FieldInfo> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }
}
