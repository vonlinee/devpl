package io.devpl.backend.utils;

import java.io.File;
import java.nio.file.Path;

public class PathUtils {

    public static boolean isDirectory(String path) {
        return path.endsWith(File.pathSeparator);
    }

    public static String toPathname(Class<?> clazz) {
        return clazz.getName().replace(".", File.separator);
    }

    public static String of(String path, String... pathSegment) {
        if (path == null) {
            return of("", pathSegment);
        }
        return Path.of(path, pathSegment).toString();
    }

    public static String toRelative(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    /**
     * 是否是绝对路径 TODO 待实现
     * /aaa/bb表示相对路径，但是上级路径是根路径
     * aaa/bb表示相对路径
     * 以/结尾表示目录
     *
     * @param path 路径
     * @return 是否是绝对路径
     */
    public static boolean isAbsolute(String path) {
        return false;
    }
}
