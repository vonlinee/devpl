package io.devpl.codegen.template;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
     * 渲染字符串模板
     *
     * @param template  模板内容，不能为null或者空
     * @param arguments 模板参数
     * @return 渲染结果
     */
    default String evaluate(String template, TemplateArguments arguments) throws TemplateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            render(getTemplate(template, true), arguments, outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    /**
     * 渲染并输出到指定位置
     *
     * @param templateSource 模板
     * @param arguments      模板参数
     * @param outputStream   输出位置
     */
    void render(TemplateSource templateSource, TemplateArguments arguments, OutputStream outputStream) throws TemplateException;

    /**
     * 同render方法
     *
     * @param name      模板名称，通过模板名称加载模板
     * @param arguments 模板参数
     * @param fos       输出位置
     * @throws TemplateException 渲染出错
     */
    default void render(String name, Map<String, Object> arguments, OutputStream fos) throws TemplateException {
        render(getTemplate(name, false), new TemplateArgumentsMap(arguments, false), fos);
    }

    /**
     * 渲染模板，输出为字符串
     *
     * @param template  模板
     * @param arguments 模板参数
     * @return 渲染结果
     */
    default String render(TemplateSource template, TemplateArguments arguments) throws TemplateException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            render(template, arguments, os);
            return os.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

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
     * @param nameOrTemplate 模板名称或者字符串模板
     * @param st             是否是字符串模板, 为true，则将nameOrTemplate参数视为模板文本，为false则将nameOrTemplate参数视为模板名称
     * @return 模板实例
     */
    @NotNull
    default TemplateSource getTemplate(String nameOrTemplate, boolean st) {
        return TemplateSource.UNKNOWN;
    }
}
