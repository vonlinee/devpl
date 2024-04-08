package io.devpl.codegen.generator;

import java.util.Map;

/**
 * 给予模板的生成文件类型
 * 指定模板信息
 * 单个类型，单个模板文件
 *
 * @see TemplateBasedTableFileGenerator
 */
public interface TemplateBasedTargetFile extends TargetFile {

    /**
     * 该文件类型使用的模板
     * 模板存放目录，不同的模板引擎分开存放 template/模板文件扩展名/具体的模板
     *
     * @return 模板，可以是文件路径，或者字符串模板
     */
    String getTemplate();

    /**
     * 从生成目标提取所需的模板参数
     *
     * @param target 生成目标
     * @return 模板参数
     */
    Map<String, Object> getTemplateArguments(GenerationTarget target);

    /**
     * @param context 上下文
     * @return TemplateBasedTableFileGenerator
     * @see TemplateBasedTableFileGenerator
     */
    @Override
    default FileGenerator getFileGenerator(Context context) {
        return new TemplateBasedTableFileGenerator();
    }
}
