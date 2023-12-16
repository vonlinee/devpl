package io.devpl.codegen.strategy;

import io.devpl.codegen.core.DynamicContextSupport;

/**
 * 项目文件定位策略
 */
public interface ProjectFileLocator extends DynamicContextSupport {

    /**
     * 根目录
     *
     * @param rootDir 根目录
     */
    default void setRootDirectory(String rootDir) {
    }

    /**
     * 设置模块名称
     *
     * @param module 模块名称
     */
    default void setModuleName(String module) {
    }

    /**
     * 返回绝对路径
     *
     * @param filename 文件名
     * @param ext      文件扩展名
     * @return 文件存放位置
     */
    String locate(String filename, String ext);
}
