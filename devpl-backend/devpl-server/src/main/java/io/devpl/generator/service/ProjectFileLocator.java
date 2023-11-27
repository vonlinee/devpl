package io.devpl.generator.service;

/**
 * 项目文件存放位置
 * 用于决定生成的代码放在什么位置
 */
public interface ProjectFileLocator {

    void setRoot(String rootDir);

    String getRoot();

    String locate(String filename, String extension);
}
