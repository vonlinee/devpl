package io.devpl.codegen.template.beetl;

import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.Template;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Beetl 模板引擎实现文件输出
 * <a href="http://ibeetl.com/guide/#/beetl/basic?id=%e5%ae%89%e8%a3%85">...</a>
 */
public class BeetlTemplateEngine extends AbstractTemplateEngine {

    private static Method method;

    static {
        try {
            method = GroupTemplate.class.getDeclaredMethod("getTemplate", Object.class);
        } catch (NoSuchMethodException e) {
            try {
                // 3.2.x 方法签名修改成了object,其他低版本为string
                method = GroupTemplate.class.getDeclaredMethod("getTemplate", String.class);
            } catch (NoSuchMethodException exception) {
                throw new RuntimeException("beetl template engine init failed", exception);
            }
        }
    }

    private final GroupTemplate groupTemplate;

    public BeetlTemplateEngine() {
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(new ClasspathResourceLoader("/"), cfg);
        } catch (IOException e) {
            log.error("初始化模板引擎失败:", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void evaluate(String template, Object arguments, Writer writer) {

    }

    @Override
    public void render(Template template, Object arguments, OutputStream outputStream) {

    }

    @Override
    public String getTemplateFileExtension() {
        return ".btl";
    }

    @Override
    public @NotNull Template getTemplate(String name, boolean stringTemplate) {
        try {
            org.beetl.core.Template template = (org.beetl.core.Template) method.invoke(groupTemplate, name);
            return new BeetlTemplate(template);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return super.getTemplate(name, stringTemplate);
        }
    }
}
