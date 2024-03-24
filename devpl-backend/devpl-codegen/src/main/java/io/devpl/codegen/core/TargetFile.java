package io.devpl.codegen.core;

/**
 * 生成目标文件类型
 */
public interface TargetFile {

    /**
     * 文件名
     *
     * @return 文件名
     */
    String getFilename(Context context);

    /**
     * 文件扩展名
     *
     * @return 文件扩展名
     */
    String getExtension(Context context);

    /**
     * 编码信息
     *
     * @return 文件编码信息
     */
    String getEncoding(Context context);
}
