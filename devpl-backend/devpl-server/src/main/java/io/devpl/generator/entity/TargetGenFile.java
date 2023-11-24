package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 生成的文件信息，以及使用的模板信息
 */
@Data
@TableName("target_gen_file")
public class TargetGenFile implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 代码生成任务ID
     */
    @TableField(value = "task_id")
    private String taskId;

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
    @TableField(exist = false)
    private String templateName;

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
    private boolean builtin = true;
}
