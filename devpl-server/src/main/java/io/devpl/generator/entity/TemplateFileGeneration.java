package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 生成的文件信息，以及使用的模板信息
 * 模板文件生成
 */
@Data
@TableName("template_file_generation")
public class TemplateFileGeneration {
    /**
     * 主键ID
     */
    @TableId
    private Long pid;

    /**
     * 代码生成任务ID
     */
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
     * 保存路径
     */
    private String savePath;

    /**
     * 备注信息
     */
    private String remark;
}
