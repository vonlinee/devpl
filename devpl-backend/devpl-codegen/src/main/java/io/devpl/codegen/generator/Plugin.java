package io.devpl.codegen.generator;

import io.devpl.codegen.generator.file.GeneratedFile;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public interface Plugin extends ContextAware {

    /**
     * 插件名称
     *
     * @return 插件名称
     */
    default String getName() {
        return toString();
    }

    /**
     * 注入配置属性
     *
     * @param properties 配置属性
     */
    void setProperties(Properties properties);

    /**
     * Plugins can implement this method to override any of the default attributes, or change the results of database
     * introspection, before any code generation activities occur. Attributes are listed as static Strings with the
     * prefix ATTR_ in IntrospectedTable.
     *
     * <p>A good example of overriding an attribute would be the case where a user wanted to change the name of one
     * of the generated classes, change the target package, or change the name of the generated SQL map file.
     *
     * <p><b>Warning:</b> Anything that is listed as an attribute should not be changed by one of the other plugin
     * methods. For example, if you want to change the name of a generated example class, you should not simply change
     * the Type in the <code>modelExampleClassGenerated()</code> method. If you do, the change will not be reflected
     * in other generated artifacts.
     *
     * @param target the generation target
     */
    default void initialize(GenerationTarget target) {
    }

    /**
     * 校验插件配置
     *
     * @param warnings 警告信息
     * @return 是否校验成功
     */
    default boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 该插件是否支持指定的GenerationTarget实例
     *
     * @param target 生成目标
     * @return 是否在指定的生成目标生效
     */
    default boolean support(GenerationTarget target) {
        return true;
    }

    /**
     * If false, the table will be skipped in code generation.
     *
     * @param unit the current generation unit
     * @return true if code should be generated for this table, else false
     */
    default boolean shouldGenerate(GenerationTarget unit) {
        return true;
    }

    /**
     * 插件针对生成目标，选择增加或者删除某些文件
     *
     * @param unit  生成单元
     * @param files 总的文件信息
     * @see Plugin#generateFiles(GenerationTarget) 只能选择是否添加文件
     */
    default void generateFiles(GenerationTarget unit, List<GeneratedFile> files) {
    }

    /**
     * @param unit 生成单元
     * @return 由当前Plugin处理后的文件信息
     */
    default List<GeneratedFile> generateFiles(GenerationTarget unit) {
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
