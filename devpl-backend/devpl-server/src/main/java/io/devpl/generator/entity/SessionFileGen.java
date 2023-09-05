package io.devpl.generator.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 单个文件类型 - 单个模板ID
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-08-18
 */
@Data
@TableName("file_gen_session")
public class SessionFileGen {
    /**
     * 会话ID
     */
    @TableId(value = "session_id", type = IdType.AUTO)
    private Integer sessionId;

    /**
     * 生成文件ID
     */
    @TableField(value = "gen_file_id")
    private Integer genFileId;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private Integer templateId;

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    private String taskId;
}
