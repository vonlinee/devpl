package com.baomidou.mybatisplus.generator.engine;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResource;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import java.nio.charset.StandardCharsets;

/**
 * 字符串模板
 *
 * @see StringResourceLoader
 */
public class StringTemplateSource implements StringResourceRepository, TemplateSource {

    String templateContent;

    public StringTemplateSource(String content) {
        this.templateContent = content;
    }

    @Override
    public String getTemplate() {
        return templateContent;
    }

    @Override
    public void setTemplate(String template) {
        templateContent = template;
    }

    @Override
    public StringResource getStringResource(String name) {
        String nameSects[] = name.split(",");
        try {
            String str = "";//这里可以根据传进来的参数name，查询模板(模板可以在数据库或文件中)
            return new StringResource(str, this.getEncoding());
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("获取模板异常");
        }
    }

    @Override
    public void putStringResource(String name, String body) {

    }

    @Override
    public void putStringResource(String name, String body, String encoding) {

    }

    @Override
    public void removeStringResource(String name) {

    }

    @Override
    public void setEncoding(String encoding) {

    }

    @Override
    public String getEncoding() {
        return StandardCharsets.UTF_8.name();
    }
}
