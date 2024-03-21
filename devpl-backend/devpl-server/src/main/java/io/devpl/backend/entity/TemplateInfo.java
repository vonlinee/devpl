package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devpl.backend.domain.TemplateEngineType;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

/**
 * 模板信息
 */
@Getter
@Setter
@TableName(value = "template_info")
public class TemplateInfo extends DBEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private String templateId;

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
     * 本地文件模板存放路径，相对路径
     */
    @TableField(value = "template_file_path")
    private String templateFilePath;

    /**
     * 模板内容: 如果为空则读取templatePath指定路径的文件作为模板内容
     */
    @TableField(value = "content", jdbcType = JdbcType.LONGVARCHAR)
    private String content;

    /**
     * 技术提供方，例如Apache Velocity, Apache FreeMarker
     *
     * @see TemplateEngineType
     */
    @TableField(value = "provider")
    private String provider;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否内置模板
     */
    @TableField(value = "is_internal")
    private Boolean internal;

    //============================================= 非数据库字段 =================================================

    /**
     * 生成代码的路径
     */
    @TableField(exist = false)
    private String generatorPath;

    @JsonIgnore
    public boolean isStringTemplate() {
        return this.getType() == 2;
    }

    @JsonIgnore
    public boolean isFileTemplate() {
        return this.getType() == 1;
    }
}
