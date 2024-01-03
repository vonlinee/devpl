package io.devpl.fxui.model;

import io.devpl.fxui.controller.mbg.ProjectLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 项目配置
 */
@Getter
@Setter
public class ProjectConfiguration {

    /**
     * 配置名称
     */
    private String name;

    /**
     * 项目所在根目录
     */
    private String projectRootFolder;

    /**
     * 父包名
     */
    private String parentPackage;

    /**
     * 实体类所在包名
     */
    private String entityPackageName;

    /**
     * 实体类存放目录：相对目录
     */
    private String entityPackageFolder;

    /**
     * Mapper接口包名
     */
    private String mapperPackageName;

    /**
     * Mapper接口存放目录
     */
    private String mapperFolder;

    /**
     * 映射XML文件包名
     */
    private String mapperXmlPackage;

    /**
     * 映射XML文件存放目录
     */
    private String mapperXmlFolder;

    /**
     * 项目结构
     */
    private ProjectLayout projectLayout;
}
