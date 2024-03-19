package io.devpl.codegen.template.freemarker;

import freemarker.template.Configuration;
import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Freemarker 模板引擎实现文件输出
 *
 * @see freemarker.cache.TemplateLoader
 */
public class FreeMarkerTemplateEngine extends AbstractTemplateEngine {

    private final Configuration configuration;

    public FreeMarkerTemplateEngine() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // FreeMarker 加载模板目录主要可以通过三种方式来实现，分别是基于文件系统、基于 Web 项目以及基于类路径。
        // 指定加载模板的类
        try {
            configuration.setDirectoryForTemplateLoading(new File("/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
