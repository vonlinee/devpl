package com.baomidou.mybatisplus.generator.engine;

/**
 * 文件模板
 */
public class FileTemplateSource implements TemplateSource {

    String templatePath;

    @Override
    public void setTemplate(String template) {
        this.templatePath = template;
    }

    @Override
    public String getTemplate() {
        return templatePath;
    }
}
