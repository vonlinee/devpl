package io.devpl.codegen.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 生成目标文件类型
 */
public interface TargetFile {

    /**
     * 写入到流中
     *
     * @param stream 输出流
     */
    void write(OutputStream stream) throws IOException;

    /**
     * 文件名
     *
     * @return 文件名
     */
    String getFilename();

    /**
     * 文件扩展名
     *
     * @return 文件扩展名
     */
    String getExtension();

    /**
     * 编码信息
     *
     * @return 文件编码信息
     */
    String getEncoding();
}
