package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 字段组信息表
 */
@Getter
@Setter
@TableName(value = "field_group")
public class FieldGroup implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父组ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 字段组名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 字段数量
     */
    @TableField(exist = false)
    private Integer fieldCount;

    /**
     * 该组的字段列表
     */
    @TableField(exist = false)
    private List<GroupField> fields;
}
