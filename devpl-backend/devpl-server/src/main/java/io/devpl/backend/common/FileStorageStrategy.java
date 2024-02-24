package io.devpl.backend.common;

import io.devpl.backend.domain.FileNode;

import java.io.InputStream;
import java.util.List;
import java.util.StringJoiner;

/**
 * 文件存储策略
 */
public interface FileStorageStrategy {

    /**
     * 获取文件存储路径分隔符
     *
     * @return 文件存储路径分隔符
     */
    String getPathSeparator();

    /**
     * 去除名称的前后可能存在的路径分隔符
     *
     * @param name 路径片段名称
     * @return 文件节点名称
     */
    default String toFileNodeName(String name) {
        if (name == null || name.isEmpty()) {
            return "null";
        }
        String pathSeparator = getPathSeparator();
        if (name.startsWith(pathSeparator)) {
            name = name.substring(pathSeparator.length());
        }
        if (name.endsWith(pathSeparator)) {
            name = name.substring(0, name.length() - pathSeparator.length());
        }
        return name;
    }

    /**
     * 拼接多段路径
     *
     * @param pathSegments 路径片段
     * @return 拼接后的路径
     */
    default String concat(String... pathSegments) {
        StringJoiner path = new StringJoiner(getPathSeparator());
        for (String segment : pathSegments) {
            path.add(toFileNodeName(segment));
        }
        return path.toString();
    }

    /**
     * 文件路径是否有效
     *
     * @param path 文件路径
     * @return 文件路径是否有效
     */
    boolean isPathValid(String path);

    /**
     * 将非法的文件路径正常化
     *
     * @param path 非法的文件路径
     * @return 转换后的文件路径
     */
    String normalize(String path);

    /**
     * 文件路径是否存在
     *
     * @param path 文件路径
     * @return 文件路径是否存在
     */
    boolean exists(String path);

    /**
     * 是否是文件
     *
     * @param path 路径
     * @return 是否是文件
     */
    boolean isFile(String path);

    /**
     * 是否是目录
     *
     * @param path 路径
     * @return 是否是目录
     */
    boolean isDirectory(String path);

    /**
     * 将文件流写到路径指向的位置
     *
     * @param path        文件存储路径
     * @param inputStream 文件流，内部不关闭该输入流，只进行读写
     */
    void write(String path, InputStream inputStream);

    /**
     * 文件树形结构
     *
     * @param rootPath 开始目录
     * @return 文件树，相对路径
     */
    List<FileNode> getFileTree(String rootPath);
}
