package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表文件生成记录表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 */
@Getter
@Setter
@TableName("table_file_generation")
public class TableFileGeneration implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表ID
     */
    @TableField(value = "table_id")
    private Long tableId;

    /**
     * 文件生成关联ID
     *
     * @see TemplateFileGeneration#getId()
     */
    @TableField(value = "generation_id")
    private Long generationId;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 保存路径: 目录
     */
    @TableField(value = "save_path")
    private String savePath;

    /**
     * 更新时间
     */
    @JsonIgnore
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @JsonIgnore
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否逻辑删除
     */
    @JsonIgnore
    @TableField(value = "is_deleted")
    private boolean deleted;

    public TableFileGeneration() {
        this.setDeleted(false);
    }

    // ================================= 非数据库字段 =================================

    /**
     * 模板ID
     */
    @TableField(exist = false)
    private Long templateId;

    /**
     * 模板名称
     */
    @TableField(exist = false)
    private String templateName;
}
