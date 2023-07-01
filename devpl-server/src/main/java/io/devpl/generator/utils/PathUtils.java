package io.devpl.generator.utils;

import java.io.File;

public class PathUtils {

    public static boolean isDirecotry(String path) {
        return path.endsWith(File.pathSeparator);
    }
}
