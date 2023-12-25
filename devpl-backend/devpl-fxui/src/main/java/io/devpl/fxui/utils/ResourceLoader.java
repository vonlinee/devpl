package io.devpl.fxui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 用于资源加载
 */
public class ResourceLoader {

    private static String ROOT = "";

    /**
     * 加载资源作为URL
     * @param name 不要以/开头
     * @return 文件URL
     */
    public static URL load(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    public static InputStream openStream(String name) throws IOException {
        URL url = load(name);
        return url.openStream();
    }

    /**
     * 加载资源为URL
     * @param clazz 以Class的包名作为根路径
     * @param name  相对路径名称
     * @return
     */
    public static URL load(Class<?> clazz, String name) {
        if (clazz == null) {
            return load(name);
        }
        String packageName = clazz.getPackage().getName();
        String directoryName = packageName.replace(".", "/");

        if (name == null || name.isEmpty()) {
            throw new RuntimeException("the path is empty!");
        }

        String finalFileName = directoryName + "/" + name;
        return load(finalFileName);
    }

    /**
     * 设置根目录
     * @param rootDir 根目录
     */
    public static void setRootDirectory(String rootDir) {
        if (rootDir == null || rootDir.isEmpty()) {
            throw new RuntimeException("the root path is empty!");
        }
        ROOT = rootDir;
    }

    /**
     * 解析文件路径名
     * @param path
     * @return
     */
    public static String resolve(String path) {
        if (path == null) {
            return path;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    /**
     * 标准化文件路径
     * @param path
     * @return
     */
    public static String normalize(String path) {
        return path;
    }
}
