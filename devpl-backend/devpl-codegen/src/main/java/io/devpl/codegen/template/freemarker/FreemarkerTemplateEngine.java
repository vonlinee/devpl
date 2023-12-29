package io.devpl.codegen.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.devpl.codegen.template.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    public void evaluate(String template, TemplateArguments arguments, Writer writer) {

    }

    @Override
    public void render(TemplateSource templateSource, TemplateArguments arguments, OutputStream outputStream) {
        if (templateSource instanceof FreeMarkerTemplateSource) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                templateSource.render(this, arguments, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public @NotNull TemplateSource getTemplate(String name, boolean stringTemplate) {
        try {
            Template template = configuration.getTemplate(name);
            return new FreeMarkerTemplateSource(template);
        } catch (IOException e) {
            log.error("cannot find template by template name {}", name);
        }
        return TemplateSource.UNKNOWN;
    }

    @Override
    public String getTemplateFileExtension() {
        return ".ftl";
    }
}
