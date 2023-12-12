package com.baomidou.mybatisplus.generator.codegen;

import java.io.File;

/**
 * 代码生成过程回调
 */
public interface ActionCallback {

    /**
     * 写文件
     *
     * @param file 文件
     */
    default void writeFile(File file) {
    }
}
