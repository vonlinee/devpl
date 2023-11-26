package io.devpl.generator.config.template;

import io.devpl.generator.entity.TemplateInfo;
import lombok.Data;

import java.util.List;

/**
 * 代码生成信息
 */
@Data
public class GeneratorInfo {

    /**
     * 开发者信息
     */
    private DeveloperInfo developer;

    /**
     * 模板信息
     */
    private List<TemplateInfo> templates;
}
