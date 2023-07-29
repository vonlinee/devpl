package io.devpl.generator.utils;

import java.io.File;
import java.nio.file.Path;

public class Paths {

    public static boolean isDirecotry(String path) {
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

    /**
     * 是否是绝对路径 TODO 待实现
     * /aaa/bb表示相对路径，但是上级路径是根路径
     * aaa/bb表示相对路径
     * 以/结尾表示目录
     * @param path 路径
     * @return 是否是绝对路径
     */
    public static boolean isAbsolute(String path) {
        return false;
    }
}
