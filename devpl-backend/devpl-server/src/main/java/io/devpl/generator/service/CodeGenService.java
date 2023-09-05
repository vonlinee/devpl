package io.devpl.generator.service;

import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.domain.FileNode;

import javax.annotation.Nonnull;
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

    /**
     * 下载代码
     * @param tableId 表ID
     * @param zip     压缩包
     */
    void downloadCode(Long tableId, ZipOutputStream zip);

    /**
     * 生成某个表的文件
     * @param tableId gen_table主键
     * @return 生成文件的根目录
     */
    String startCodeGeneration(Long tableId);

    /**
     * 获取渲染的数据模型
     * @param tableId 表ID
     */
    Map<String, Object> prepareDataModel(Long tableId);

    /**
     * 获取文件树
     * @param workPath 根路径
     * @return 文件树节点
     */
    List<FileNode> getFileTree(String workPath);

    /**
     * 获取路径的文本
     * @param path 文本
     * @return 文本
     */
    String getFileContent(String path);
}
