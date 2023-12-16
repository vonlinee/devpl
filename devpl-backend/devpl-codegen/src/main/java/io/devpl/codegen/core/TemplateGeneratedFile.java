package io.devpl.codegen.core;

import io.devpl.codegen.template.TemplateEngine;
import lombok.Getter;
import lombok.Setter;

import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 基于模板的生成文件信息
 */
@Setter
@Getter
public class TemplateGeneratedFile extends GeneratedFile {

    /**
     * 模板名称
     */
    private String template;

    /**
     * 使用的模板引擎
     */
    private TemplateEngine templateEngine;

    @Override
    public void write(Writer writer, Charset charset) {

    }
}
