package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 字段信息表
 */
@Getter
@Setter
@TableName(value = "field_info")
public final class FieldInfo implements Serializable {

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
     * 字段Key，区分大小写，不包含嵌套 比如 user.age ，层级关系由 id 和 parentId 进行确定
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
     * 字面值
     */
    @TableField(value = "literal_value")
    private String literalValue;

    /**
     * 默认值
     */
    @TableField(value = "default_value")
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
     * 是否是叶子节点
     */
    @TableField(exist = false)
    private boolean leaf = true;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否逻辑删除
     */
    @TableField(value = "is_deleted")
    private Boolean deleted;

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

    public void setChildren(List<FieldInfo> children) {
        if (children == null || children.isEmpty()) {
            this.leaf = true;
        }
        this.children = children;
    }

    public List<FieldInfo> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }
}
