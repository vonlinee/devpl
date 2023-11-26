package io.devpl.generator.service;

import io.devpl.generator.domain.FileNode;

import java.util.List;

/**
 * 文件存储 Service
 */
public interface FileStorageService {

    /**
     * 获取文件树
     *
     * @param workPath 根路径
     * @return 文件树节点
     */
    List<FileNode> getFileTree(String workPath);
}
