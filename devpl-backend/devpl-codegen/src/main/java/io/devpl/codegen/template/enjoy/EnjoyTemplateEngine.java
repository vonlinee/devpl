package io.devpl.codegen.template.enjoy;

import com.jfinal.template.Engine;
import io.devpl.codegen.template.AbstractTemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * enjoy 模板引擎实现文件输出
 *
 * @author flyinke
 * @since 2022-06-16
 */
public class EnjoyTemplateEngine extends AbstractTemplateEngine {

    private final Engine engine;

    public EnjoyTemplateEngine() {
        engine = Engine.createIfAbsent("codegen", Engine::setToClassPathSourceFactory);
    }

    @Override
    public void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull OutputStream outputStream) throws Exception {
        String str = engine.getTemplate(templatePath).renderToString(objectMap);
        try (OutputStreamWriter ow = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            writer.append(str);
        }
    }

    @Override
    public String getTemplateFileExtension() {
        return ".ej";
    }
}

