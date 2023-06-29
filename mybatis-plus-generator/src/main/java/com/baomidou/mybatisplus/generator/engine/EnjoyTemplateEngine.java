package com.baomidou.mybatisplus.generator.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.Context;
import com.jfinal.template.Engine;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;

/**
 * enjoy 模板引擎实现文件输出
 * @author flyinke
 * @since 2022-06-16
 */
public class EnjoyTemplateEngine extends AbstractTemplateEngine {

    private Engine engine;

    @Override
    public @NotNull AbstractTemplateEngine init(@NotNull Context configBuilder) {
        engine = Engine.createIfAbsent("mybatis-plus-generator", e -> {
            e.setToClassPathSourceFactory();
        });
        return this;
    }

    @Override
    public void merge(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull OutputStream outputStream) throws Exception {
        String str = engine.getTemplate(templatePath).renderToString(objectMap);
        try (OutputStreamWriter ow = new OutputStreamWriter(outputStream, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            writer.append(str);
        }
    }

    @Override
    public @NotNull String templateFilePath(@NotNull String filePath) {
        final String dotVm = ".ej";
        return filePath.endsWith(dotVm) ? filePath : filePath + dotVm;
    }
}

