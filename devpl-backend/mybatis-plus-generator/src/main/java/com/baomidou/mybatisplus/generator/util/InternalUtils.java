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
}
