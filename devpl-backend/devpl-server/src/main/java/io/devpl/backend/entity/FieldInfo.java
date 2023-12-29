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

    /**
     * 字段注释信息
     */
    @TableField(exist = false)
    private String comment;
}
