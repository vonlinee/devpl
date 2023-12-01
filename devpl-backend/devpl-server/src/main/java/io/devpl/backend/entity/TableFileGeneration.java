package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 表文件生成记录表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 */
@Getter
@Setter
@TableName("table_file_generation")
public class TableFileGeneration {
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
