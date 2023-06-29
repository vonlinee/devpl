package io.devpl.generator.common;

/**
 * 文件存储策略
 */
public interface FileStorageStrategy {

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
}
