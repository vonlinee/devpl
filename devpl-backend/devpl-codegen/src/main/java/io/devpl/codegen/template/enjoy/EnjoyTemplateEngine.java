package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Engine;
import io.devpl.codegen.template.AbstractTemplateEngine;
import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateException;
import io.devpl.codegen.template.TemplateSource;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * enjoy 模板引擎实现文件输出
 */
public class EnjoyTemplateEngine extends AbstractTemplateEngine {

    private final Engine engine;

    public EnjoyTemplateEngine() {
        engine = Engine.createIfAbsent("codegen", Engine::setToClassPathSourceFactory);
    }

    @Override
    public void render(TemplateSource templateSource, TemplateArguments arguments, OutputStream outputStream) {

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
    public @NotNull TemplateSource getTemplate(String name, boolean stringTemplate) {
        return new EnjoyTemplateSource(engine.getTemplate(name));
    }

    @Override
    public String getTemplateFileExtension() {
        return ".ej";
    }
}

