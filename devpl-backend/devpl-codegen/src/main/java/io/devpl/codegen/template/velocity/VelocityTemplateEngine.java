package io.devpl.codegen.template.velocity;

import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.StringTemplateSource;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateSource;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
            // VelocityEngineVersion.VERSION;
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException e) {
            // velocity1.x的生成格式错乱 https://github.com/baomidou/generator/issues/5
            log.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }
    }

    static final String ST = "StringTemplate";

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

    /**
     * 渲染模板
     * 通过文件模板进行查找，如果找不到则找字符串模板
     * 使用Velocity.evaluate无法使用自定义指令功能
     *
     * @param templateSource 模板类型
     * @param arguments      模板参数
     * @param outputStream   输出位置，不会关闭流
     * @see VelocityStringResourceRepository
     */
    @Override
    public void render(TemplateSource templateSource, TemplateArguments arguments, OutputStream outputStream) {
        // 渲染字符串模板
        if (templateSource.isStringTemplate()) {
            VelocityContext context = new VelocityContext(arguments.asMap());
            Velocity.evaluate(context, new OutputStreamWriter(outputStream), ST, templateSource.getContent());
            return;
        }
        templateSource = templateSource.getSource();
        if (templateSource instanceof VelocityTemplateSource) {
            Template vt = engine.getTemplate(templateSource.getName());
            try (Writer writer = new OutputStreamWriter(outputStream)) {
                vt.merge(new VelocityContext(arguments.asMap()), writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getTemplateFileExtension() {
        return ".vm";
    }

    /**
     * @param nameOrTemplate 模板名称或者字符串模板
     * @param stringTemplate 是否是字符串模板, 为true，则将nameOrTemplate参数视为模板文本，为false则将nameOrTemplate参数视为模板名称
     * @return 模板
     * @see TemplateSource#getName()
     * @see VelocityEngine#getTemplate(String, String)
     */
    @Override
    public @NotNull TemplateSource getTemplate(String nameOrTemplate, boolean stringTemplate) {
        if (stringTemplate) {
            return new StringTemplateSource(nameOrTemplate, null);
        }
        Template template = engine.getTemplate(nameOrTemplate, StandardCharsets.UTF_8.name());
        return new VelocityTemplateSource(template);
    }
}
