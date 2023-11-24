package com.baomidou.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.generator.config.builder.Context;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Beetl 模板引擎实现文件输出
 *
 * @author yandixuan
 * @since 2018-12-16
 */
public class BeetlTemplateEngine extends AbstractTemplateEngine {

    static final Logger log = LoggerFactory.getLogger(VelocityTemplateEngine.class);

    private static Method method;

    static {
        try {
            method = GroupTemplate.class.getDeclaredMethod("getTemplate", Object.class);
        } catch (NoSuchMethodException e) {
            try {
                // 3.2.x 方法签名修改成了object,其他低版本为string
                method = GroupTemplate.class.getDeclaredMethod("getTemplate", String.class);
            } catch (NoSuchMethodException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private GroupTemplate groupTemplate;

    @Override
    public @NotNull AbstractTemplateEngine init(@NotNull Context configBuilder) {
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(new ClasspathResourceLoader("/"), cfg);
        } catch (IOException e) {
            log.error("初始化模板引擎失败:", e);
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, OutputStream outputStream) throws Exception {
        Template template = (Template) method.invoke(groupTemplate, templatePath);
        template.binding(objectMap);
        template.renderTo(outputStream);
    }

    @Override
    public @NotNull String templateFilePath(@NotNull String filePath) {
        return filePath + ".btl";
    }

    @Override
    public void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream) {

    }
}
