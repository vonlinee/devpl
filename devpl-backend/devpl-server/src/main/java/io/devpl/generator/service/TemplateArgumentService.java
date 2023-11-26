package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.generator.entity.TemplateArgument;

import java.util.Map;

/**
 * 模板参数 Service
 */
public interface TemplateArgumentService extends IService<TemplateArgument> {

    void initialize(Long templateId, Long generationId, Map<String, Object> argumentsMap);

    String serialize(Object value);
}
