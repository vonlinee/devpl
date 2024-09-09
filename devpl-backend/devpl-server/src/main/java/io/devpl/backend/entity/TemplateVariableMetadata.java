package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板变量表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 */
@Getter
@Setter
@TableName("template_variable_metadata")
public class TemplateVariableMetadata extends Entity {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private Integer templateId;

    /**
     * 变量key, 一般为出现在模板中的变量名,单个模板内唯一
     */
    @TableField(value = "key")
    private String key;

    /**
     * 变量名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 参数值,默认参数值, 未提供该参数时使用此值
     */
    @TableField(value = "default_value")
    private String defaultValue;

    /**
     * 数据类型, 统一所有的数据类型
     */
    @TableField(value = "data_type_id")
    private Long dataTypeId;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;
}
