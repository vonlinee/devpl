package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class TemplateInfo extends DatabaseTableEntity {

    /**
     * 模板ID
     */
    @TableId(value = "template_id", type = IdType.AUTO)
    private Integer templateId;

    /**
     * 模板名称：唯一
     */
    @TableField(value = "template_name")
    private String templateName;

    /**
     * 模板类型: 1-文件模板 2-字符串模板
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 模板名称
     */
    @TableField(value = "path")
    private String templatePath;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 技术提供方，例如Apache Velocity, Apache FreeMarker
     * @see io.devpl.generator.domain.TemplateProvider
     */
    @TableField(value = "provider")
    private String provider;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 生成代码的路径
     */
    @TableField(exist = false)
    private String generatorPath;
}
