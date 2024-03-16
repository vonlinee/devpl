package io.devpl.common.utils;

import java.io.File;

public interface ProjectAnalyser {

    /**
     * 是否是项目根目录
     *
     * @param entryFile 入口File
     * @return 是否是项目根目录
     */
    boolean isProjectRootDirectory(File entryFile);

    /**
     * 解析项目结构
     *
     * @param entryFile 项目工程入口文件
     * @return 项目信息
     */
    ProjectModule analyse(File entryFile);
}
