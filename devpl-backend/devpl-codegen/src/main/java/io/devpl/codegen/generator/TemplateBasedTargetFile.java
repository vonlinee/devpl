package io.devpl.codegen.generator;

/**
 * 给予模板的生成文件类型
 * 指定模板信息
 */
public interface TemplateBasedTargetFile extends TargetFile {

    /**
     * 该文件类型使用的模板
     * 模板存放目录，不同的模板引擎分开存放 template/模板文件扩展名/具体的模板
     *
     * @return 模板，可以是文件路径，或者字符串模板
     */
    String getTemplate();
}
