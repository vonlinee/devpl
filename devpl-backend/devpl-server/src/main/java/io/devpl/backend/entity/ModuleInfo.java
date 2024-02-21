package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 模块信息
 */
@Getter
@Setter
@TableName("module_info")
public class ModuleInfo implements Serializable {

    /**
     * 项目id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 组
     */
    private String groupId;

    private String artifactId;

    private String version;
}
