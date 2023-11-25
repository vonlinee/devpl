package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 模板文件生成关联表
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
     * 父节点ID
     */
    private Long parentId;

    /**
     * 文件生成项名称
     */
    @TableField(value = "item_name")
    private String itemName;

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
    private Integer builtin;

}
