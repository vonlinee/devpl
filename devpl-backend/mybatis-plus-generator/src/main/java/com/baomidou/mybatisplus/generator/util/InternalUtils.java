package com.baomidou.mybatisplus.generator.util;

import java.io.File;

public class InternalUtils {

    public static String getDesktop() {
        return System.getProperty("user.home") + File.separator + "Desktop";
    }
}
