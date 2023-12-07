package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 模型和字段关联
 *
 * @see ModelInfo
 * @see FieldInfo
 */
@Getter
@Setter
@TableName("model_field")
public class ModelField {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型ID
     */
    private Long modelId;

    /**
     * 字段ID
     */
    private Long fieldId;

    // =================== 非数据库字段 ============================

    /**
     * 字段Key
     */
    @TableField(exist = false)
    private String fieldKey;

    /**
     * 字段名，中文名
     */
    @TableField(exist = false)
    private String fieldName;

    /**
     * 字段数据类型
     */
    @TableField(exist = false)
    private String dataType;
}
