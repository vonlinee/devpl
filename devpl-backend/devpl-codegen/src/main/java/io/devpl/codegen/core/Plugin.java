package io.devpl.codegen.core;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public interface Plugin {

    void setContext(Context context);

    void setProperties(Properties properties);

    /**
     * 初始化生成单元
     *
     * @param unit 生成单元
     */
    default void initialize(GenerationUnit unit) {
    }

    /**
     * If false, the table will be skipped in code generation.
     *
     * @param unit the current generation unit
     * @return true if code should be generated for this table, else false
     */
    default boolean shouldGenerate(GenerationUnit unit) {
        return true;
    }

    /**
     * @param unit     生成单元
     * @param previous 前一个Plugin生成的文件信息
     * @return 由当前Plugin处理后的文件信息
     */
    default List<GeneratedFile> generateFiles(GenerationUnit unit, List<GeneratedFile> previous) {
        return Collections.emptyList();
    }

    /**
     * 插件优先级
     *
     * @return 用于调整插件执行顺序，值越小，优先级越高
     */
    default int getPriority() {
        return 0;
    }
}
