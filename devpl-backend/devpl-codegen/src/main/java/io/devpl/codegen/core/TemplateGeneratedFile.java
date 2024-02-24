package io.devpl.codegen.core;

import io.devpl.codegen.template.TemplateArguments;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.Template;
import io.devpl.sdk.io.IOUtils;
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

    /**
     * 模板用到的参数
     */
    private TemplateArguments templateArguments;

    @Override
    public void write(Writer writer, Charset charset) {
        Template ts = templateEngine.getTemplate(template, false);
        try {
            templateEngine.render(ts, templateArguments, writer);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
