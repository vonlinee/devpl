package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 字段组和字段关联关系
 */
@Getter
@Setter
@TableName(value = "group_field")
public class GroupField {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字段组ID
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 字段组ID
     */
    @TableField(value = "field_id")
    private Long fieldId;

    /**
     * 排序号
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    // 非数据库字段

    @TableField(exist = false)
    private String fieldKey;

    @TableField(exist = false)
    private String fieldName;

    @TableField(exist = false)
    private String dataType;

    @TableField(exist = false)
    private String comment;

    public GroupField(Long groupId, Long fieldId) {
        this.groupId = groupId;
        this.fieldId = fieldId;
    }
}
