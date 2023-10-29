package io.devpl.generator.jdbc;

import java.io.File;

public class URLClassLoaderTest {
    private static final String DRIVER_FILE_8 = "D:\\Temp\\drivers\\mysql\\8.0.18\\mysql-connector-java-8.0.18.jar";
    private static final String DRIVER_FILE_5 = "D:\\Temp\\drivers\\mysql\\5.1.49\\mysql-connector-java-5.1.49.jar";

    public static void test() throws Exception {
        // 先new一个URLClassLoader加载mysql8的驱动, 注意创建URLClassLoader的时候显示指定parent为空
        File mysql8DriverFile = new File(DRIVER_FILE_8);
        JdbcDriverClassLoader mysql8ClassLoader = new JdbcDriverClassLoader(mysql8DriverFile.toURL());
        Class mysql8DriverClass = mysql8ClassLoader.loadClass("com.mysql.cj.jdbc.Driver");

        // 再new一个URLClassLoader加载mysql5的驱动, 注意创建URLClassLoader的时候显示指定parent为空
        File mysql5DriverFile = new File(DRIVER_FILE_5);
        JdbcDriverClassLoader mysql5ClassLoader = new JdbcDriverClassLoader(mysql5DriverFile.toURL());
        Class mysql5DriverClass = mysql5ClassLoader.loadClass("com.mysql.jdbc.Driver");

        // 比较加载的两个是否是同一个类，并观察实际加载类的ClassLoader
        System.out.println(mysql8DriverClass + " " + mysql8DriverClass.getClassLoader());
        System.out.println(mysql5DriverClass + " " + mysql5DriverClass.getClassLoader());
        System.out.println(mysql8DriverClass.equals(mysql5DriverClass));
    }
}
