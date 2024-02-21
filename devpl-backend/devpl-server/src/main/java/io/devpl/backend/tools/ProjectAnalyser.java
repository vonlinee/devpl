package io.devpl.backend.tools;

import io.devpl.backend.domain.ProjectModule;

import java.io.File;

public interface ProjectAnalyser {

    /**
     * 解析项目结构
     *
     * @param entryFile 项目工程入口文件
     * @return 项目信息
     */
    ProjectModule analyse(File entryFile);
}
