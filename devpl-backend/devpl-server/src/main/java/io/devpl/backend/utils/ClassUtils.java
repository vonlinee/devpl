package io.devpl.backend.utils;

import io.devpl.sdk.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ClassUtils {

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
        // cglib
        , "org.springframework.cglib.proxy.Factory", "javassist.util.proxy.ProxyObject"
        // javassist
        , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    private ClassUtils() {
    }

    /**
     * getConstructor 方法入参是可变长参数列表，对应类中构造方法的入参类型，这里使用无参构造。
     * newInstance 返回的是泛型 T，取决于 clazz 的类型 Class<T>。这里直接用 Object 接收了。
     * 调用默认方法创建对象实例
     *
     * @param clazz Class对象
     * @return 创建的对象实例
     */
    public static <T> T instantiate(Class<T> clazz) throws RuntimeException {
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("failed to instantiate class " + clazz + " cause:", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to instantiate class " + clazz + " cause: no default constructor in Class[" + clazz + "]", e);
        }
    }

    /**
     * 获取类名
     *
     * @param className className 全类名，格式aaa.bbb.ccc.xxx
     * @return ignore
     */
    public static String getSimpleName(String className) {
        return !StringUtils.hasText(className) ? null : className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * 开头不能是数字，不能包含.
     *
     * @param identifier 类名
     * @return 是否是合法的Java标识符
     */
    public static boolean isValidJavaIdentifier(String identifier) {
        // 确定是否允许将指定字符作为 Java 标识符中的首字符。
        if (identifier == null || identifier.isEmpty() || !Character.isJavaIdentifierStart(identifier.charAt(0))) {
            return false;
        }
        int len = identifier.length();
        for (int i = 1; i < len; i++) {
            char c = identifier.charAt(i);
            if (c == '.') {
                return false;
            }
            // 确定指定字符是否可以是 Java 标识符中首字符以外的部分。
            if (!Character.isJavaIdentifierPart(identifier.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 不用正则判断
     * [a-zA-Z]+[0-9a-zA-Z_]*(\.[a-zA-Z]+[0-9a-zA-Z_]*)*\.[a-zA-Z]+[0-9a-zA-Z_]*(\$[a-zA-Z]+[0-9a-zA-Z_]*)*
     * 不能有空格，不能连续两个.
     * 支持内部类，不支持lambda
     * <a href="https://blog.csdn.net/zhanglianyu00/article/details/77499295">...</a>
     *
     * @param str 字符串
     * @return 是否是全限定类名
     */
    public static boolean isValidQualifiedClassName(String str) {
        // 空字符串或者以.开头或结尾
        if (str == null || str.length() == 0 || str.startsWith(".") || str.endsWith(".")) {
            return false;
        }
        boolean flag = true;
        char[] chars = str.toCharArray();
        int curDotIndex = 0;
        // O(n)
        for (int i = 1; i < chars.length - 1; i++) {
            if (chars[i] == '.') {
                curDotIndex = i;
                if (chars[i - 1] == '.' || Character.isJavaIdentifierPart(chars[i - 1])) {
                    return false;
                }
                break;
            } else if (chars[i] == ' ') {
                // 空格
                return false;
            }
        }
        return curDotIndex == 0;
    }

    /**
     * 获取类的包名
     *
     * @param qualifiedClassName 全限定类名
     * @return 全类名的包名，比如java.util.List，返回java.util
     */
    public static String getPackageName(String qualifiedClassName) {
        if (qualifiedClassName == null || qualifiedClassName.length() == 0) {
            return qualifiedClassName;
        }
        if (!qualifiedClassName.contains(".")) {
            return "";
        }
        return getSimpleName(qualifiedClassName);
    }

    /**
     * <p>
     * 请仅在确定类存在的情况下调用该方法
     * </p>
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     */
    public static Class<?> forName(String name) {
        try {
            return Class.forName(name, false, getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
            }
        }
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class，返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 获取当前对象的 class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        Objects.requireNonNull(clazz, "Class must not be null");
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you clearly prefer a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * {@code Class.forName}, which accepts a {@code null} ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (only {@code null} if even the system
     * ClassLoader isn't accessible)
     * @see Thread#getContextClassLoader()
     * @see ClassLoader#getSystemClassLoader()
     * @since 3.3.2
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * @param path 例如 com/mysql/cj/conf/ConnectionUrl
     * @return 以点分隔的类名
     */
    public static String toClassName(String path) {
        String replace = path.replace("/", ".");
        if (replace.endsWith(".class")) {
            int index = replace.lastIndexOf(".");
            replace = replace.substring(0, index);
        }
        return replace;
    }
}
