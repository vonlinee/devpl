package io.devpl.sdk.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * A class to simplify access to resources through the classloader.
 */
public final class Resources {

    private static final Class<Resources> RESOURCES_CLASS = Resources.class;

    /**
     * Resources包名
     */
    private static final String PACKAGE_NAME = RESOURCES_CLASS.getPackageName();

    /**
     * 项目类路径的根路径
     */
    private static final String CLASSPATH_ROOT = Objects.requireNonNull(RESOURCES_CLASS.getResource("/")).getPath();

    /**
     * 项目的根路径，如果嵌套项目，则返回父项目所在路径
     */
    private static final String PROJECT_ROOT_PATH = new File("").getAbsolutePath();

    /**
     * 默认类加载器: Thread.currentThread().getContextClassLoader()
     */
    private static ClassLoader defaultClassLoader;

    private Resources() {
    }

    /**
     * Returns the default classloader (may be null).
     *
     * @return The default classloader
     */
    public static ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    /**
     * Sets the default classloader
     *
     * @param defaultClassLoader - the new default ClassLoader
     */
    public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        Resources.defaultClassLoader = defaultClassLoader;
    }

    /**
     * Loads a class
     *
     * @param className - the class to load
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found (duh!)
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        } catch (Exception e) {
            // Ignore. Failsafe below.
        }
        if (clazz == null) {
            // 抛出ClassNotFoundException
            clazz = Class.forName(className);
        }
        return clazz;
    }

    private static ClassLoader getClassLoader() {
        if (defaultClassLoader != null) {
            return defaultClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    public static URL getClasspathResource(String name) {
        return getClasspathResource(name, false);
    }

    /**
     * 获取类路径下的资源
     *
     * @param name 相对路径
     * @return URL
     */
    public static URL getClasspathResource(String name, boolean ignored) {
        File file = new File(CLASSPATH_ROOT + resolveName(name));
        if (!file.exists() && !ignored) {
            throw new RuntimeException("资源[" + CLASSPATH_ROOT + name + "]不存在");
        }
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URL urlOfFile(File file) {
        try {
            URL url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL getProjectResource(String name) {
        return getProjectResource(name, false);
    }

    public static URL getProjectResource(String name, boolean ignored) {
        name = resolveName(name);
        try {
            return new File(PROJECT_ROOT_PATH + name).toURI().toURL();
        } catch (MalformedURLException e) {
            if (!ignored)
                throw new RuntimeException("资源[" + PROJECT_ROOT_PATH + name + "]不存在");
            return null;
        }
    }

    /**
     * Add a package name prefix if the name is not absolute Remove leading "/"
     * if name is absolute
     *
     * @see
     */
    private static String resolveName(String name) {
        if (!name.startsWith("/")) {
            if (!PACKAGE_NAME.isEmpty()) {
                name = PACKAGE_NAME.replace('.', '/') + "/" + name;
            }
        } else {
            // 去掉开头的 /
            name = name.substring(1);
        }
        return name;
    }
}
