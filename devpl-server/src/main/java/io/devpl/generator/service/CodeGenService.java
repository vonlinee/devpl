package io.devpl.generator.service;

import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.domain.FileNode;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 */
public interface CodeGenService {

    /**
     * 获取生成器配置信息
     * @return 生成器配置信息
     */
    GeneratorInfo getGeneratorInfo();

    void downloadCode(Long tableId, ZipOutputStream zip);

    /**
     * 生成某个表的文件
     * @param tableId gen_table主键
     */
    void generatorCode(Long tableId);

    /**
     * 获取渲染的数据模型
     * @param tableId 表ID
     */
    Map<String, Object> prepareDataModel(Long tableId);

    List<FileNode> getFileTree(String workPath);

    String getFileContent(String path);
}
