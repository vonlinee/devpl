package io.devpl.backend.config.template;

import io.devpl.backend.entity.TemplateInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 代码生成信息
 */
@Setter
@Getter
public class GeneratorInfo {

    /**
     * 模板信息
     */
    private List<TemplateInfo> templates;
}
