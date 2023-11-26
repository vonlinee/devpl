package com.baomidou.mybatisplus.generator.engine;

/**
 * 字符串模板
 */
public class StringTemplateSource implements TemplateSource {

    String content;

    public StringTemplateSource(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String template) {
        content = template;
    }
}
