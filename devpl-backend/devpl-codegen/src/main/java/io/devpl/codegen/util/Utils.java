package io.devpl.codegen.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 仅在此模块内部使用
 */
public class Utils {

    /**
     * 获取桌面目录
     *
     * @return 桌面路径
     */
    public static String getDesktopDirectory() {
        return System.getProperty("user.home") + File.separator + "Desktop";
    }

    /**
     * 参数不能为空
     *
     * @param arg  字符串
     * @param msg  错误信息模板
     * @param args 错误信息模板参数
     */
    public static void notBlank(String arg, String msg, Object... args) {
        if (arg == null || arg.isBlank()) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    /**
     * 参数不能为空
     *
     * @param collection collection集合
     * @param msg        错误信息模板
     * @param args       错误信息模板参数
     */
    public static void notEmpty(Collection<?> collection, String msg, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    /**
     * 参数不能为空
     *
     * @param collection collection集合
     * @param msg        错误信息模板
     * @param args       错误信息模板参数
     */
    public static void notEmpty(Map<?, ?> collection, String msg, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    public static <T> void notEmpty(T[] arr, String msg, Object... args) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    /**
     * 按 JavaBean 规则来生成 get 和 set 方法后面的属性名称
     * 需要处理一下特殊情况：
     * <p>
     * 1、如果只有一位，转换为大写形式
     * 2、如果多于 1 位，只有在第二位是小写的情况下，才会把第一位转为小写
     * <p>
     */
    public static String getCapitalName(String name) {
        if (name.length() == 1) {
            return name.toUpperCase();
        }
        if (Character.isLowerCase(name.charAt(1))) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
        return name;
    }

    /**
     * 加载properties文件
     *
     * @param file properties文件
     * @return properties
     */
    public static Properties loadProperties(File file) {
        if (!file.exists()) {
            throw new RuntimeException(String.format("数据库连接配置文件%s不存在", file));
        }
        Properties properties = new Properties();
        try (FileReader fr = new FileReader(file)) {
            properties.load(fr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
