package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板信息
 */
@Getter
@Setter
@TableName(value = "template_info")
public class TemplateInfo extends EntityBase {

    /**
     * 模板ID
     */
    @TableId(value = "template_id")
    private String templateId;

    /**
     * 模板名称
     */
    @TableField(value = "template_name")
    private String templateName;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 生成代码的路径
     */
    @TableField(exist = false)
    private String generatorPath;
}
