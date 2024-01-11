package io.devpl.codegen.util;

import io.devpl.sdk.lang.RuntimeReflectiveOperationException;
import io.devpl.sdk.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * 获取类名
     *
     * @param className className 全类名
     * @return ignore
     */
    public static String getSimpleName(String className) {
        return StringUtils.isBlank(className) ? null : className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * <p>
     * 请仅在确定类存在的情况下调用该方法
     * </p>
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     */
    public static Class<?> toClassConfident(String name) {
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

    public static Optional<Class<?>> tryLoadClass(String className) {
        try {
            return Optional.of(ClassUtils.toClassConfident(className));
        } catch (Exception e) {
            // 当父类实体存在类加载器的时候,识别父类实体字段，不存在的情况就只有通过指定superEntityColumns属性了。
        }
        return Optional.empty();
    }

    /**
     * 根据类名创建对象
     *
     * @param className 全限定类名
     * @param superType 类的父类型
     * @param <T>       父类型对象
     * @return 类名表示的对象
     * @throws RuntimeException 包装反射异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className, Class<T> superType) throws RuntimeException {
        Utils.notBlank(className, "类名为空");
        Objects.requireNonNull(superType, "父类型为空");
        return (T) tryLoadClass(className).map(clazz -> {
            if (!superType.isAssignableFrom(clazz)) {
                throw new RuntimeException("创建对象失败, 类型" + clazz + "与类型" + superType + "不兼容");
            }
            T instance;
            try {
                Constructor<?> constructor = clazz.getConstructor();
                instance = (T) constructor.newInstance();
            } catch (NoSuchMethodException e) {
                return new RuntimeException("不存在默认构造函数");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("创建对象失败", e);
            }
            return instance;
        }).orElseThrow(() -> new RuntimeException("创建对象" + className + "失败"));
    }

    /**
     * 实例化指定的类
     *
     * @param clazz 指定类
     * @param <T>   类型
     * @return 对象
     */
    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeReflectiveOperationException(e);
        }
    }
}
