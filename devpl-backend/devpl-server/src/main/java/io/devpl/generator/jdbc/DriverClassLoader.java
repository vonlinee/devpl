package io.devpl.generator.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;

/**
 * URLClassLoader之所以不能实现类加载隔离原因就是URLClassLoader遵循了父类委派机制，
 * 那我们要实现类加载隔离很自然的想法就是破坏父类委托机制，优先自己加载，自己加载不到的时候再使用父类加载器，
 * 这其实就是类加载隔离的核心机制。知道原理之后实现其实很简单，这里提供一种最简单的实现方式，
 * URLClassLoader在创建的时候如果不显示指定父类加载器，则父类加载器是AppClassLoader，
 * 但URLClassLoader也支持显示指定父类加载器，最简单的实现就是将父类加载器显示指定为null，这样可以让URLClassLoader自己去加载jar包：
 * <a href="https://blog.csdn.net/zhongguoyu27/article/details/116545162">...</a>
 */
public class DriverClassLoader extends URLClassLoader {

    /**
     * 驱动加载隔离
     *
     * @param url 指向驱动jar包文件
     */
    public DriverClassLoader(URL url) {
        // 指定父类加载器为null
        this(new URL[]{url}, null);
    }

    DriverClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    private Driver loadDriver(String driverClassName) {
        try {
            Class<?> driverClass = this.loadClass(driverClassName);
            Constructor<?> constructor = driverClass.getConstructor();
            return (Driver) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
