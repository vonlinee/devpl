package com.baomidou.mybatisplus.generator.util;

import java.io.File;

/**
 * 仅在此模块内部使用
 */
public class InternalUtils {

    /**
     * 获取桌面目录
     *
     * @return 桌面路径
     */
    public static String getDesktopDirectory() {
        return System.getProperty("user.home") + File.separator + "Desktop";
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
}
