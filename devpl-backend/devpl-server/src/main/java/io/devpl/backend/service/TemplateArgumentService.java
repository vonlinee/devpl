package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TemplateArgument;

import java.util.Map;

/**
 * 模板参数 Service
 */
public interface TemplateArgumentService extends IService<TemplateArgument> {

    /**
     * 初始化模板参数
     *
     * @param templateId   模板ID
     * @param generationId 生成ID
     * @param argumentsMap 模板参数
     */
    void initialize(Long templateId, Long generationId, Map<String, Object> argumentsMap);

    /**
     * 将模板参数值序列化为字符串
     *
     * @param value 参数值
     * @return 字符串
     */
    String serialize(Object value);
}
