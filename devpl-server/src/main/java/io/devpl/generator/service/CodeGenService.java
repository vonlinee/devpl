package io.devpl.generator.service;

import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.domain.FileNode;

import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 */
public interface CodeGenService {

    /**
     * 获取生成器配置信息
     *
     * @return 生成器配置信息
     */
    GeneratorInfo getGeneratorInfo();

    void downloadCode(Long tableId, ZipOutputStream zip);

    void generatorCode(Long tableId);

    List<FileNode> getFileTree(String workPath);

    String getFileContent(String path);
}
