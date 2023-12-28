package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateSource;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
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
 * <a href="https://velocity.apache.org/engine/devel/getting-started.html">...</a>
 * <a href="https://velocity.apache.org/engine/devel/user-guide.html">...</a>
 * <p>
 * org/apache/velocity/runtime/defaults/velocity.properties
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {
    static final Logger log = LoggerFactory.getLogger(VelocityTemplateEngine.class);

    static {
        try {
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException e) {
            // velocity1.x的生成格式错乱 https://github.com/baomidou/generator/issues/5
            log.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }
    }

    private final VelocityEngine engine;
    private final StringResourceRepository stringTemplates = new VelocityStringResourceRepository();

    public VelocityTemplateEngine() {
        engine = createEngine();
    }

    private VelocityEngine createEngine() {
        Properties properties = new Properties();
        try (InputStream is = this.getClass().getResourceAsStream("velocity.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Can't load custom velocity config file from classpath", e);
        }

        VelocityEngine engine = new VelocityEngine(properties);
        /**
         * 按StringResourceLoader的javadoc文档配置无效，手动添加添加，这里手动添加
         * 通过下面方式获取:
         * VelocityStringResourceRepository repository = (VelocityStringResourceRepository) engine.getApplicationAttribute("devpl");
         */
        engine.setApplicationAttribute("devpl", stringTemplates);
        return engine;
    }

    @Override
    public void render(@NotNull String name, @NotNull Map<String, Object> arguments, @NotNull OutputStream outputStream) throws Exception {
        Template template = engine.getTemplate(name, StandardCharsets.UTF_8.name());
        try (OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8); BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(arguments), writer);
        }
    }

    @Override
    public void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream) {
        if (template instanceof VelocityTemplateSource) {
            VelocityContext context = new VelocityContext(arguments.asMap());
            Velocity.evaluate(context, new OutputStreamWriter(outputStream), "mystring", template.getContent());
        }
    }

    /**
     * 直接渲染字符串模板
     * 通过文件模板进行查找，如果找不到则找字符串模板
     * 使用Velocity.evaluate无法使用自定义指令功能
     *
     * @see VelocityStringResourceRepository
     */
    @Override
    public String render(String template, TemplateArguments params) {
        if (template == null) {
            return "";
        }
        StringWriter output = new StringWriter();
        Template vt = engine.getTemplate(template);
        vt.merge(new VelocityContext(params.asMap()), output);
        return output.toString();
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

    @Override
    public String getTemplateFileExtension() {
        return ".vm";
    }

    @Override
    public @NotNull TemplateSource getTemplate(String name) {
        Template template = engine.getTemplate(name);
        return new VelocityTemplateSource(template);
    }
}
