package io.devpl.generator.service.impl;

import io.devpl.generator.domain.FileNode;
import io.devpl.generator.service.FileStorageService;
import io.devpl.generator.utils.SecurityUtils;
import io.devpl.sdk.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    /**
     * 获取文件树
     *
     * @param rootPath 工作路径
     * @return 文件节点列表
     */
    @Override
    public List<FileNode> getFileTree(String rootPath) {
        File root = new File(rootPath);
        List<FileNode> node = new ArrayList<>();
        recursive(root, node);
        return node;
    }

    /**
     * 递归生成文件树
     *
     * @param path 目录
     * @param node 保存节点
     */
    private void recursive(File path, List<FileNode> node) {
        if (path.isDirectory()) {
            File[] list = path.listFiles();
            if (list == null) {
                return;
            }
            for (File file : list) {
                FileNode fileNode = new FileNode();
                fileNode.setKey(SecurityUtils.base64Encode(file.getAbsolutePath()));
                fileNode.setLabel(file.getName());
                fileNode.setPath(file.getAbsolutePath());
                node.add(fileNode);
                if (file.isDirectory()) {
                    fileNode.setIsLeaf(false);
                    fileNode.setSelectable(false);
                    List<FileNode> children = new ArrayList<>();
                    fileNode.setChildren(children);
                    recursive(file, children);
                } else {
                    fileNode.setIsLeaf(true);
                    fileNode.setSelectable(true);
                    fileNode.setExtension(FileUtils.getExtensionName(file, "txt"));
                }
            }
        } else {
            FileNode fileNode = new FileNode();
            fileNode.setKey(SecurityUtils.base64Encode(path.getAbsolutePath()));
            fileNode.setLabel(path.getName());
            fileNode.setIsLeaf(true);
            fileNode.setSelectable(true);
            node.add(fileNode);
        }
    }
}
