package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 模板文件生成关联表
 * 通过模板进行文件生成
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-25
 */
@Data
@TableName("template_file_generation")
public class TemplateFileGeneration {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField(value = "template_name")
    private String templateName;

    /**
     * 数据填充策略
     */
    @TableField(value = "fill_strategy")
    private String dataFillStrategy;

    /**
     * 保存路径
     */
    @TableField(value = "save_path")
    private String savePath;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否内置
     */
    @TableField(value = "builtin")
    private Boolean builtin;

    /**
     * 配置表记录主键ID
     */
    @TableField(value = "config_table_id")
    private Long configTableId;

    /**
     * 配置表名称
     */
    @TableField(value = "config_table_name")
    private String configTableName;
}
