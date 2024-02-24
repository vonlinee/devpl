package io.devpl.backend.service.impl;

import io.devpl.backend.common.FileStorageStrategy;
import io.devpl.backend.domain.FileNode;
import io.devpl.backend.service.FileStorageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Resource
    FileStorageStrategy fileStorageStrategy;

    /**
     * 获取文件树
     *
     * @param rootPath 工作路径
     * @return 文件节点列表
     */
    @Override
    public List<FileNode> getFileTree(String rootPath) {
        return fileStorageStrategy.getFileTree(rootPath);
    }

    /**
     * 获取文件列表
     *
     * @param parent 父级目录
     * @return 文件节点列表
     */
    @Override
    public List<FileNode> listFiles(String parent) {
        return fileStorageStrategy.listFiles(parent);
    }
}
