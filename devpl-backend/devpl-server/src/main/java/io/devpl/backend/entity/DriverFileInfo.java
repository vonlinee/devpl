package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件信息
 */
@Setter
@Getter
@TableName(value = "driver_file_info")
public class DriverFileInfo extends Entity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件唯一ID
     */
    private String fileId;

    /**
     * 原始文件名
     */
    private String fileName;
}
