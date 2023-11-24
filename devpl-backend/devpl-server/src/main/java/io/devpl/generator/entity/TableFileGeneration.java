package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 表文件生成记录表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 */
@Data
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
     * 模板ID
     */
    @TableField(value = "template_id")
    private Long templateId;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 保存路径
     */
    @TableField(value = "save_path")
    private String savePath;
}
