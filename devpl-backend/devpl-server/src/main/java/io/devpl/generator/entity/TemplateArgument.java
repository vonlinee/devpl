package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 模板参数表，模板实际的参数值
 */
@Getter
@Setter
@TableName("template_argument")
public class TemplateArgument {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private Long templateId;

    /**
     * 模板ID
     */
    @TableField(value = "generation_id")
    private Long generationId;

    /**
     * 参数key, 一般为出现在模板中的变量名,单个模板内唯一
     */
    @TableField(value = "var_key")
    private String varKey;

    /**
     * 参数值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 数据类型
     */
    @TableField(value = "data_type_id")
    private Long dataTypeId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
