package io.devpl.backend.common;

import java.io.InputStream;
import java.util.StringJoiner;

/**
 * 文件存储策略
 */
public interface FileStorageStrategy {

    /**
     * 获取文件存储路径分隔符
     * @return 文件存储路径分隔符
     */
    String getPathSeparator();

    /**
     * 拼接多段路径
     * @param pathSegments 路径片段
     * @return 拼接后的路径
     */
    default String concat(String... pathSegments) {
        StringJoiner path = new StringJoiner(getPathSeparator());
        for (String pathSegment : pathSegments) {
            path.add(pathSegment);
        }
        return path.toString();
    }

    /**
     * 文件路径是否有效
     * @param path 文件路径
     * @return 文件路径是否有效
     */
    boolean isPathValid(String path);

    /**
     * 将非法的文件路径正常化
     * @param path 非法的文件路径
     * @return 转换后的文件路径
     */
    String normalize(String path);

    /**
     * 文件路径是否存在
     * @param path 文件路径
     * @return 文件路径是否存在
     */
    boolean exists(String path);

    /**
     * 将文件流写到路径指向的位置
     * @param path        文件存储路径
     * @param inputStream 文件流，内部不关闭该输入流，只进行读写
     */
    void write(String path, InputStream inputStream);
}
