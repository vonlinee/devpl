package io.devpl.backend.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

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

    /**
     * 判断路径是否有包含关系
     *
     * @param p1 路径1
     * @param p2 路径2
     * @return 是否重叠
     */
    public static boolean contains(Path p1, Path p2) {
        final int nameCount1 = p1.getNameCount();
        final int nameCount2 = p2.getNameCount();
        int count = Math.min(nameCount1, nameCount2);
        int _count = 0;
        for (int i = 1; i <= count; i++) {
            // 从尾部开始判断
            Path _p1 = p1.getName(nameCount1 - i);
            Path _p2 = p2.getName(nameCount2 - i);
            if (!Objects.equals(_p1, _p2)) {
                return false;
            }
            _count++;
        }
        return _count == count;
    }

    /**
     * 路径片段是否交叉
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean isNameOverlapped(Path p1, Path p2) {
        final int nameCount1 = p1.getNameCount();
        final int nameCount2 = p2.getNameCount();

        // 从长的那个路径开始
        if (nameCount1 > nameCount2) {
            String pathname = p2.toString();
            for (int i = 0; i < nameCount1; i++) {
                Path name = p1.getName(nameCount1 - i - 1);

                if (pathname.contains(name.toString())) {

                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Path path1 = Path.of("D:/Temp", "aaa/bbb/ccc");
        Path path2 = Path.of("bbb/ccc");

        System.out.println(contains(path1, path2));
    }
}
