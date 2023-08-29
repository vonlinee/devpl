package com.baomidou.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.Context;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * Velocity 模板引擎实现文件输出
 * <a href="https://velocity.apache.org/engine/1.7/user-guide.html">Velocity 1.7</a>
 * @author hubin
 * @since 2018-01-10
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {
    private VelocityEngine velocityEngine;

    static final Logger log = LoggerFactory.getLogger(VelocityTemplateEngine.class);

    String VM_LOAD_PATH_KEY = "resource.loader.file.class";
    String VM_LOAD_PATH_VALUE = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    static {
        try {
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException e) {
            // velocity1.x的生成格式错乱 https://github.com/baomidou/generator/issues/5
            log.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }
    }

    public VelocityTemplateEngine() {
        velocityEngine = createEngine();
    }

    @Override
    public @NotNull VelocityTemplateEngine init(@NotNull Context context) {
        return this;
    }

    private VelocityEngine createEngine() {
        Properties p = new Properties();
        p.setProperty(VM_LOAD_PATH_KEY, VM_LOAD_PATH_VALUE);
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
        p.setProperty(Velocity.ENCODING_DEFAULT, StandardCharsets.UTF_8.name());
        p.setProperty(Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.name());
        p.setProperty("resource.loader.file.unicode", Boolean.TRUE.toString());
        return new VelocityEngine(p);
    }

    @Override
    public void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull OutputStream outputStream) throws Exception {
        Template template = velocityEngine.getTemplate(templatePath, StandardCharsets.UTF_8.name());
        try (OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8); BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
    }

    @Override
    public @NotNull String templateFilePath(@NotNull String filePath) {
        final String dotVm = ".vm";
        return filePath.endsWith(dotVm) ? filePath : filePath + dotVm;
    }

    @Override
    public void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream) {
        if (template instanceof StringTemplateSource) {
            VelocityContext context = new VelocityContext(arguments.asMap());
            Velocity.evaluate(context, new OutputStreamWriter(outputStream), "mystring", template.getTemplate());
        }
    }

    @Override
    public String render(TemplateSource template, TemplateArguments arguments) {
        String result;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            render(template, arguments, baos);
            result = baos.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
