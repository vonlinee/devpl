package io.devpl.backend.service;

import io.devpl.backend.domain.FileNode;

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

    /**
     * 获取文件列表
     *
     * @param parent 父路径
     * @return 文件树节点
     */
    List<FileNode> listFiles(String parent);
}
