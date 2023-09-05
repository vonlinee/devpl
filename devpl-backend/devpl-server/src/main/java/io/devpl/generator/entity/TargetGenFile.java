package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 生成的文件信息，以及使用的模板信息
 * 模板文件生成
 * 对生成的文件类型进行分类，便于批处理
 */
@Data
@TableName("target_gen_file")
public class TargetGenFile {
    /**
     * 主键ID
     */
    @TableId
    private Long pid;

    /**
     * 代码生成任务ID
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField(exist = false)
    private String templateName;

    /**
     * 保存路径
     */
    private String savePath;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否内置
     */
    private boolean builtin = true;
}
