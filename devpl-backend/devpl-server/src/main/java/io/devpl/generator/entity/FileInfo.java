package io.devpl.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文件信息
 */
@Setter
@Getter
@TableName(value = "file_info")
public class FileInfo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 文件唯一ID
     */
    private String fileId;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型（image/jpg, image/png, video/mp4, xsl,doc等)
     */
    private String fileType;

    /**
     * 文件大小（单位:kb）
     */
    private String fileSize;

    /**
     * 存储路径，绝对路径或相对路径
     */
    private String filePath;

    /**
     * 文件相对路径，域名+此字段为该资源的请求地址
     */
    private String relativePath;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 文件状态标记，0=正常，1=文件已物理删除
     */
    private Integer status;
}