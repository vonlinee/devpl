package io.devpl.codegen.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.devpl.codegen.template.AbstractTemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Freemarker 模板引擎实现文件输出
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {
    private final Configuration configuration;

    public FreemarkerTemplateEngine() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, "/");
    }

    @Override
    public void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull OutputStream outputStream) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        template.process(objectMap, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    @Override
    public String getTemplateFileExtension() {
        return ".ftl";
    }
}
