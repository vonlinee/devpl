package io.devpl.codegen.core;

/**
 * 给予模板的生成文件类型
 * 指定模板信息
 */
public interface TemplateBasedTargetFile extends TargetFile {

    String getTemplate();
}
