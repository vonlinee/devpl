package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 字段信息表
 */
@Data
@TableName(value = "field_info")
public class FieldInfo implements Serializable {

    /**
     * 字段ID
     */
    @TableId(value = "field_id", type = IdType.ASSIGN_ID)
    private String fieldId;

    /**
     * 字段名
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
