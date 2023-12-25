package io.devpl.fxui.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 用于资源加载
 */
public class ResourceLoader {

    /**
     * 加载类路径下的资源作为URL
     *
     * @param name 不要以/开头
     * @return 文件URL
     */
    public static URL load(String name) {
        Path path = Paths.get(new File("").getAbsolutePath(), "devpl-fxui", "src/main/resources", name);
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载资源为URL
     *
     * @param clazz 以Class的包名作为根路径
     * @param name  相对路径名称
     * @return URL
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
}
