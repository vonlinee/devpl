package io.devpl.backend.service;

import io.devpl.backend.domain.FileNode;
import io.devpl.backend.domain.param.FileGenerationParam;
import io.devpl.backend.domain.vo.FileGenerationResult;

import java.util.List;
import java.util.Map;

/**
 * 代码文件生成
 */
public interface FileGenerationService {

    /**
     * 文件生成
     *
     * @param param FileGenerationParam
     * @return FileGenerationResult
     */
    FileGenerationResult generateFile(FileGenerationParam param);

    /**
     * 生成某个表的所有文件
     *
     * @param tableId gen_table主键
     * @return 生成文件的根目录
     */
    String generateForTable(Long tableId);

    /**
     * 获取代码生成绝对路径
     *
     * @param path 相对路径
     * @return 文件绝对路径
     */
    String getAbsolutePath(String path);

    /**
     * 获取渲染的数据模型
     *
     * @param tableId 表ID
     */
    Map<String, Object> prepareDataModel(Long tableId);

    /**
     * 获取生成结果，文件树
     *
     * @param workPath 根路径
     * @return 文件树节点
     */
    List<FileNode> getGeneratedFileTree(String workPath);

    /**
     * 获取路径的文本
     *
     * @param path 文本
     * @return 文本
     */
    String getFileContent(String path);
}
