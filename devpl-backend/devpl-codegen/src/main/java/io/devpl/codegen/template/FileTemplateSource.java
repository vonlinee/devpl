package io.devpl.codegen.template;

/**
 * 文件模板
 */
public class FileTemplateSource implements TemplateSource {

    String templatePath;

    @Override
    public String getContent() {
        return templatePath;
    }

    @Override
    public void setContent(String template) {
        this.templatePath = template;
    }
}
