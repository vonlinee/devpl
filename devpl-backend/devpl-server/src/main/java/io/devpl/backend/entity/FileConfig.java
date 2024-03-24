package io.devpl.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文件配置信息
 */
@Setter
@Getter
public class FileConfig {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 文件类型限制
     * 文件类型（mine-type标准)，为空不限制上传类型
     */
    private String fileTypeLimit;

    /**
     * kb 文件限制大小，为空不限制上传大小(但要满足框架支持的上传文件大小)
     */
    private String fileSizeLimit;

    /**
     * 服务器文件夹路径
     */
    private String path;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 访问资源路径
     */
    private String resourceRealm;

    /**
     * 是否可用(默认1可用，0禁用)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;
}
