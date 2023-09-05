package com.baomidou.mybatisplus.generator.engine;

/**
 * 字符串模板
 */
public class StringTemplateSource implements TemplateSource {

    String templateContent;

    public StringTemplateSource(String content) {
        this.templateContent = content;
    }

    @Override
    public void setTemplate(String template) {
        templateContent = template;
    }

    @Override
    public String getTemplate() {
        return templateContent;
    }
}
