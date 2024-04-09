package io.devpl.codegen.template.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.Template;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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
        // configuration.setTemplateLoader();
        try {
            configuration.setDirectoryForTemplateLoading(new File("/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Configuration configurationForStringTemplate;

    /**
     * 直接渲染字符串需要使用 StringTemplateLoader
     * 模板不可使用 {@link Configuration#getTemplate(String)}, 而是使用 {@link freemarker.template.Template#Template(String, Reader, Configuration)}
     *
     * @param template  模板内容，不能为null或者空
     * @param arguments 模板参数
     * @param writer    渲染结果
     * @see freemarker.cache.StringTemplateLoader
     */
    @Override
    public void evaluate(String template, Object arguments, Writer writer) {
        try {
            String templateName = UUID.randomUUID().toString();
            if (configurationForStringTemplate == null) {
                configurationForStringTemplate = newConfiguration();
            }
            freemarker.template.Template fm = new freemarker.template.Template(templateName, new StringReader(template), configurationForStringTemplate);
            fm.process(arguments, writer);
        } catch (IOException | TemplateException e) {
            throw io.devpl.codegen.template.TemplateException.wrap(e);
        }
    }

    /**
     * 配置 freemarker configuration
     *
     * @return Configuration
     */
    private Configuration newConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return configuration;
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
            freemarker.template.Template template;
            if (stringTemplate) {
                template = new freemarker.template.Template("", name, configuration);
            } else {
                template = configuration.getTemplate(name);
            }
            return new FreeMarkerTemplate(template);
        } catch (IOException e) {
            log.error("cannot find template by template name {}", name);
        }
        return Template.UNKNOWN;
    }

    @Override
    public @NotNull String getTemplateFileExtension() {
        return ".ftl";
    }
}
