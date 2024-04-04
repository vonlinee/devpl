package io.devpl.codegen.generator;

/**
 * 生成目标文件类型
 */
public interface TargetFile extends ContextAware {

    /**
     * 文件类型的唯一名称
     *
     * @return 文件类型的唯一名称
     */
    String name();

    /**
     * 初始化文件信息
     *
     * @param file 文件信息
     */
    void initialize(GeneratedFile file);
}
