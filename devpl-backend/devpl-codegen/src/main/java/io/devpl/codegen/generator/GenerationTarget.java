package io.devpl.codegen.generator;

import java.util.List;
import java.util.Map;

/**
 * @see TableGeneration 以表作为代码生成的对象
 */
public interface GenerationTarget {

    /**
     * 名称
     *
     * @return 生成单元名称
     */
    String getName();

    /**
     * 获取要生成的文件
     *
     * @param context 上下文
     * @return FileGenerator列表
     */
    List<FileGenerator> calculateGenerators(Context context);

    /**
     * 数据模型
     *
     * @return 数据模型
     */
    Map<String, Object> getDataModel();
}
