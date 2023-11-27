package io.devpl.generator.jdbc;

import io.devpl.generator.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.jar.Manifest;

/**
 * URLClassLoader之所以不能实现类加载隔离原因就是URLClassLoader遵循了父类委派机制，
 * 那我们要实现类加载隔离很自然的想法就是破坏父类委托机制，优先自己加载，自己加载不到的时候再使用父类加载器，
 * 这其实就是类加载隔离的核心机制。知道原理之后实现其实很简单，这里提供一种最简单的实现方式，
 * URLClassLoader在创建的时候如果不显示指定父类加载器，则父类加载器是AppClassLoader，
 * 但URLClassLoader也支持显示指定父类加载器，最简单的实现就是将父类加载器显示指定为null，这样可以让URLClassLoader自己去加载jar包：
 * <a href="https://blog.csdn.net/zhongguoyu27/article/details/116545162">...</a>
 */
public final class JdbcDriverClassLoader extends URLClassLoader {

    static final Logger logger = LoggerFactory.getLogger(JdbcDriverClassLoader.class);

    /**
     * 驱动加载隔离
     *
     * @param url 指向驱动jar包文件
     */
    public JdbcDriverClassLoader(URL url) {
        // 指定父类加载器为null
        super(new URL[]{url});
    }

    /**
     * 不仅要加载该驱动类，要加载整个jar包
     * 使用Driver实例需要使用其他的方法
     *
     * @param driverClassName 驱动类名
     * @return 驱动实例
     */
    public Driver loadDriver(String driverClassName) {
        Class<?> driverClass = null;
        try {
            driverClass = this.loadClass(driverClassName);
        } catch (ClassNotFoundException e) {
            logger.error("加载驱动失败{}", driverClassName);
        }
        if (driverClass == null) {
            return null;
        }
        return (Driver) ClassUtils.instantiate(driverClass);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    /**
     * 加载驱动实现类型会加载实现类的父类，从指定的jar包中加载
     * 会报错java.sql.Driver的ClassNotFoundException
     *
     * @param name The <a href="#binary-name">binary name</a> of the class
     * @return 驱动类
     * @throws ClassNotFoundException ClassNotFoundException
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, false);
    }

    /**
     * @param name 全限定类名，例如java.util.List，注意不能以.class结尾
     * @return Class
     * @throws ClassNotFoundException 全限定类名指示的类未找到
     * @see URLClassLoader#findClass(String)
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Package definePackage(String name, Manifest man, URL url) {
        return super.definePackage(name, man, url);
    }
}
