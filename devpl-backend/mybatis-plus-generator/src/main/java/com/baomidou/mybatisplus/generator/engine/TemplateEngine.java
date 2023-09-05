package com.baomidou.mybatisplus.generator.engine;

import java.io.OutputStream;

public interface TemplateEngine {

    String render(TemplateSource template, TemplateArguments arguments);

    /**
     * 渲染并输出到指定位置
     * @param template     模板
     * @param arguments    模板参数
     * @param outputStream 输出位置
     */
    void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream);
}
