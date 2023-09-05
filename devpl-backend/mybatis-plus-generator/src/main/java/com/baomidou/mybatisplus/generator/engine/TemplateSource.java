package com.baomidou.mybatisplus.generator.engine;

/**
 * 模板源接口
 */
public interface TemplateSource {

    void setTemplate(String template);

    /**
     * @return 可能是字符串模板，或文件路径，或者文件地址URL等
     */
    String getTemplate();
}
