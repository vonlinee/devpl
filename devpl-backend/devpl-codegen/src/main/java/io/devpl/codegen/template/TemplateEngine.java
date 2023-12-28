package io.devpl.codegen.template;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 模板引擎实现
 *
 * @see TemplateSource
 */
public interface TemplateEngine {

    /**
     * @param properties 配置参数
     */
    default void setProperties(Properties properties) {
    }

    /**
     * 渲染字符串模板，适用于模板内容少的情况，直接渲染成字符串
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
     * 同render方法
     *
     * @param arguments 模板参数
     * @param name      模板名称
     * @param fos       输出位置
     * @throws Exception 渲染出错
     */
    void render(String name, Map<String, Object> arguments, OutputStream fos) throws Exception;

    /**
     * 注册自定义指令
     *
     * @param directive 指令实现
     * @return 是否成功
     */
    default boolean registerDirective(TemplateDirective directive) {
        return false;
    }

    /**
     * 获取模板文件后缀名
     *
     * @return 模板文件后缀名
     */
    String getTemplateFileExtension();

    /**
     * 获取模板
     *
     * @param name 模板名称
     * @return 模板实例
     */
    @NotNull
    default TemplateSource getTemplate(String name) {
        return TemplateSource.UNKNOWN;
    }
}
