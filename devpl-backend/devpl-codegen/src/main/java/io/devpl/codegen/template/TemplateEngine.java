package io.devpl.codegen.template;

import java.io.OutputStream;

public interface TemplateEngine {

    /**
     * 渲染字符串模板，适用于模板内容少的情况
     *
     * @param template  模板内容，不能为null或者空
     * @param arguments 模板参数
     * @return 渲染结果
     */
    String render(String template, TemplateArguments arguments);

    /**
     * 渲染模板
     *
     * @param template  模板
     * @param arguments 模板参数
     * @return 渲染结果
     */
    String render(TemplateSource template, TemplateArguments arguments);

    /**
     * 渲染并输出到指定位置
     *
     * @param template     模板
     * @param arguments    模板参数
     * @param outputStream 输出位置
     */
    void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream);

    /**
     * 注册自定义指令
     *
     * @param directive 指令实现
     * @return 是否成功
     */
    boolean registerDirective(TemplateDirective directive);
}
