package io.devpl.codegen.template.freemarker;

import freemarker.template.Configuration;
import io.devpl.codegen.template.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Freemarker 模板引擎实现文件输出
 */
public class FreeMarkerTemplateEngine extends AbstractTemplateEngine {

    private final Configuration configuration;

    public FreeMarkerTemplateEngine() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setClassForTemplateLoading(FreeMarkerTemplateEngine.class, "/");
    }

    @Override
    public void evaluate(String template, Object arguments, Writer writer) {

    }

    @Override
    public void render(Template template, Object arguments, OutputStream outputStream) {
        if (template instanceof FreeMarkerTemplate) {
            try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
                template.render(this, arguments, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public @NotNull Template getTemplate(String name, boolean stringTemplate) {
        try {
            freemarker.template.Template template = configuration.getTemplate(name);
            return new FreeMarkerTemplate(template);
        } catch (IOException e) {
            log.error("cannot find template by template name {}", name);
        }
        return Template.UNKNOWN;
    }

    @Override
    public String getTemplateFileExtension() {
        return ".ftl";
    }
}
