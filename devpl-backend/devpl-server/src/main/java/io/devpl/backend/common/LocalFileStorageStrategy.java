package io.devpl.backend.common;

import io.devpl.backend.domain.FileNode;
import io.devpl.backend.utils.SecurityUtils;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 服务器本地文件存储
 */
@Component
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Override
    public String getPathSeparator() {
        return File.separator;
    }

    @Override
    public boolean isPathValid(String path) {
        try {
            Paths.get(path);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    @Override
    public String normalize(String path) {
        return Path.of(path).normalize().toString();
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Path.of(path));
    }

    @Override
    public boolean isFile(String path) {
        return Files.isRegularFile(Paths.get(path));
    }

    @Override
    public boolean isDirectory(String path) {
        Path fp = Paths.get(path);
        return Files.isDirectory(fp);
    }

    @Override
    public void write(String path, InputStream inputStream) {
        Path filePath = Path.of(path);
        if (!Files.exists(filePath.getParent())) {
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (OutputStream os = Files.newOutputStream(filePath)) {
            IOUtils.copy(inputStream, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FileNode> getFileTree(String rootPath) {
        File root = new File(rootPath);
        List<FileNode> node = new ArrayList<>();
        // 自定义比较器，使得目录在前，文件在后
        recursive(root, node, Comparator.comparing(f -> f.isDirectory() ? 0 : 1));
        return node;
    }

    @Override
    public List<FileNode> listFiles(String parent) {
        File parentFile = new File(parent);
        File[] files = parentFile.listFiles();
        List<FileNode> nodes = Collections.emptyList();
        if (files != null) {
            // 自定义比较器，使得目录在前，文件在后
            nodes = Arrays.stream(files).sorted(Comparator.comparing(f -> f.isDirectory() ? 0 : 1)).map(this::createFileNode).toList();
        }
        return nodes;
    }

    private FileNode createFileNode(File file) {
        FileNode node = new FileNode();
        node.setPath(file.getAbsolutePath());
        node.setLeaf(file.isFile());
        node.setKey(file.getAbsolutePath());
        node.setLabel(file.getName());
        node.setExtension(FilenameUtils.getExtension(file.getName()));
        return node;
    }


    /**
     * 递归生成文件树
     *
     * @param path 目录
     * @param node 保存节点
     */
    private void recursive(File path, List<FileNode> node, Comparator<File> sort) {
        if (path.isDirectory()) {
            File[] list = path.listFiles();
            if (list == null || list.length == 0) {
                return;
            }
            Arrays.sort(list, sort);
            for (File file : list) {
                FileNode fileNode = new FileNode();
                fileNode.setKey(SecurityUtils.base64Encode(file.getAbsolutePath()));
                fileNode.setLabel(file.getName());
                fileNode.setPath(file.getAbsolutePath());
                node.add(fileNode);
                if (file.isDirectory()) {
                    fileNode.setLeaf(false);
                    fileNode.setSelectable(false);
                    List<FileNode> children = new ArrayList<>();
                    fileNode.setChildren(children);
                    recursive(file, children, sort);
                } else {
                    fileNode.setExtension(FileUtils.getExtensionName(file, "txt"));
                }
            }
        } else {
            FileNode fileNode = new FileNode();
            fileNode.setKey(SecurityUtils.base64Encode(path.getAbsolutePath()));
            fileNode.setLabel(path.getName());
            fileNode.setLeaf(true);
            fileNode.setSelectable(true);
            node.add(fileNode);
        }
    }
}
