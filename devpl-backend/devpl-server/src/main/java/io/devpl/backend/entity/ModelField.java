package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @see ModelInfo
 * @see FieldInfo
 */
@Getter
@Setter
@TableName("model_field")
public class ModelField {

    /**
     * id
     */
    @TableId
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
     * 数据类型
     */
    @TableField(exist = false)
    private String dataType;
}
