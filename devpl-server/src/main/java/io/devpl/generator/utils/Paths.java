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
}
