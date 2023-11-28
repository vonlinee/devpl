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
    private Long id;  // 主键ID

    /**
     * 文件类型限制
     */
    private String fileTypeLimit; // 文件类型（mine-type标准)，为空不限制上传类型
    private String fileSizeLimit; //（kb）文件限制大小，为空不限制上传大小(但要满足框架支持的上传文件大小)
    private String path; // 服务器文件夹路径
    private String description;  // 描述
    private String resourceRealm; // 访问资源路径
    private Boolean enabled; // 是否可用(默认1可用，0禁用)
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime lastUpdateTime;  // 最后修改时间
}
