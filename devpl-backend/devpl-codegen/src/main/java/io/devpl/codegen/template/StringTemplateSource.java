package io.devpl.codegen.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 字符串模板
 */
public class StringTemplateSource implements TemplateSource {

    String content;

    @Nullable
    TemplateSource template;

    public StringTemplateSource(String content, @Nullable TemplateSource templateSource) {
        this.content = content;
        this.template = templateSource;
    }

    /**
     * 字符串模板的名称为其模板文本
     *
     * @return
     */
    @Override
    public @NotNull String getName() {
        return content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean isStringTemplate() {
        return template == null;
    }

    @Override
    public @NotNull TemplateSource getSource() {
        return template;
    }
}
