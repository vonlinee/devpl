package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Engine;
import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * enjoy 模板引擎实现文件输出
 */
public class JFinalEnjoyTemplateEngine extends AbstractTemplateEngine {

    private final Engine engine;

    public JFinalEnjoyTemplateEngine() {
        engine = Engine.createIfAbsent("codegen", Engine::setToClassPathSourceFactory);
    }

    @Override
    public void evaluate(String template, Object arguments, Writer writer) {

    }

    @Override
    public void render(Template template, Object arguments, OutputStream outputStream) {

    }

    @Override
    public void render(@NotNull String name, @NotNull Map<String, Object> arguments, @NotNull OutputStream outputStream) {
        String str = engine.getTemplate(name).renderToString(arguments);
        try (OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8); BufferedWriter writer = new BufferedWriter(ow)) {
            writer.append(str);
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    @Override
    public @NotNull Template getTemplate(String name, boolean stringTemplate) {
        return new EnjoyTemplate(engine.getTemplate(name));
    }

    @Override
    public @NotNull String getTemplateFileExtension() {
        return ".ej";
    }
}

