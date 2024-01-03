package io.devpl.test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestJdbcDriver {

    private static final String DRIVER_FILE_8 = "D:\\Temp\\drivers\\mysql\\5.1.49\\mysql-connector-java-5.1.49.jar";
    private static final String DRIVER_FILE_5 = "D:\\Temp\\drivers\\mysql\\8.0.18\\mysql-connector-java-8.0.18.jar";

    public static void main(String[] args) throws Exception {

//        Class<?> driverClass = Class.forName("java.sql.Driver");

        System.out.println(TestJdbcDriver.class.getClassLoader());


        // 先new一个URLClassLoader加载mysql8的驱动
        File mysql8DriverFile = new File(DRIVER_FILE_8);
        URLClassLoader mysql8ClassLoader = new URLClassLoader(new URL[]{mysql8DriverFile.toURI().toURL()}, null);
        Class<?> mysql8DriverClass = mysql8ClassLoader.loadClass("com.mysql.jdbc.Driver");

        // 再new一个URLClassLoader加载mysql5的驱动
        File mysql5DriverFile = new File(DRIVER_FILE_5);
        URLClassLoader mysql5ClassLoader = new URLClassLoader(new URL[]{mysql5DriverFile.toURI().toURL()}, null);
        Class<?> mysql5DriverClass = mysql5ClassLoader.loadClass("com.mysql.jdbc.Driver");

        // 比较加载的两个是否是同一个类，并观察实际加载类的ClassLoader
        System.out.println(mysql8DriverClass + " " + mysql8DriverClass.getClassLoader());
        System.out.println(mysql5DriverClass + " " + mysql5DriverClass.getClassLoader());
        System.out.println(mysql8DriverClass.equals(mysql5DriverClass));

    }
}
