package io.devpl.codegen.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 包含文件生成信息
 */
@Setter
@Getter
public abstract class GeneratedFile {

    /**
     * 扩展名
     */
    private String extension;

    /**
     * 文件名
     */
    private String name;

    /**
     * 输出文件内容到指定Writer
     *
     * @param writer  输出位置
     * @param charset 输出编码
     */
    public abstract void write(Writer writer, Charset charset);
}
