package io.devpl.codegen.generator;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 包含文件生成信息
 */
@Getter
@Setter
public abstract class GeneratedFile {

    /**
     * 该文件所属的项目根路径
     */
    protected String targetProject;

    /**
     * 扩展名
     */
    protected String extension;

    /**
     * 包名
     */
    protected String targetPackageName;

    /**
     * 文件名
     */
    protected String filename;

    /**
     * 目标文件类型
     *
     * @return 目标文件类型
     */
    public abstract TargetFile getFileType();

    /**
     * 输出文件内容到指定Writer
     *
     * @param writer  输出位置
     * @param charset 输出编码
     */
    public abstract void write(Writer writer, Charset charset) throws IOException;

    /**
     * Returns the entire contents of the generated file. Clients
     * can simply save the value returned from this method as the file contents.
     * Subclasses such as @see org.mybatis.generator.api.GeneratedJavaFile offer
     * more fine-grained access to file parts, but still implement this method
     * in the event that the entire contents are desired.
     *
     * @return Returns the content.
     */
    public abstract String getFormattedContent();

    /**
     * 获取文件绝对路径
     *
     * @return 文件绝对路径
     */
    public abstract String getAbsolutePath();

    /**
     * 文件扩展名
     *
     * @return 扩展名
     */
    public String getExtension() {
        return extension;
    }
}
