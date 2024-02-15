package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目名变更
 */
@Getter
@Setter
@TableName("project_info")
public class ProjectInfo implements Serializable {
    /**
     * 项目id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目标识
     */
    private String projectCode;

    /**
     * 项目包名
     */
    private String projectPackage;

    /**
     * 项目路径
     */
    private String projectPath;

    /**
     * 变更项目名
     */
    private String modifyProjectName;

    /**
     * 变更标识
     */
    private String modifyProjectCode;

    /**
     * 变更包名
     */
    private String modifyProjectPackage;

    /**
     * 排除文件
     */
    private String exclusions;

    /**
     * 变更文件
     */
    private String modifySuffix;

    /**
     * 版本号
     */
    private String version;

    /**
     * 后端路径
     */
    private String backendPath;

    /**
     * 前端路径
     */
    private String frontendPath;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 构建工具类型 1-Maven，2-Gradle
     */
    @TableField(value = "build_tool")
    private Integer buildTool;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 项目的子模块信息
     */
    @TableField(exist = false)
    private List<ModuleInfo> modules;
}
