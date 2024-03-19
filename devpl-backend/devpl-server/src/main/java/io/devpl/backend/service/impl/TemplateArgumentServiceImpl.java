package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateArgumentMapper;
import io.devpl.backend.entity.TemplateArgument;
import io.devpl.backend.service.TemplateArgumentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TemplateArgumentServiceImpl extends ServiceImpl<TemplateArgumentMapper, TemplateArgument> implements TemplateArgumentService {

    /**
     * 初始化模板的参数
     *
     * @param templateId   模板ID
     * @param argumentsMap 模板参数
     */
    @Override
    public List<TemplateArgument> initialize(Long templateId, Long generationId, Map<String, Object> argumentsMap) {
        final LocalDateTime now = LocalDateTime.now();
        List<TemplateArgument> arguments = new ArrayList<>();
        for (Map.Entry<String, Object> entry : argumentsMap.entrySet()) {
            TemplateArgument argument = new TemplateArgument();
            argument.setTemplateId(templateId);
            argument.setGenerationId(generationId);
            argument.setVarKey(entry.getKey());
            argument.setValue(this.serialize(entry.getValue()));
            argument.setCreateTime(now);
            argument.setUpdateTime(now);
            arguments.add(argument);
        }
        return arguments;
    }

    public boolean isSimpleType(Object value) {
        return value instanceof Number || value instanceof String;
    }

    @Override
    public String serialize(Object value) {
        String valueString = String.valueOf(value);
        if (valueString.length() > 500) {
            return valueString.substring(0, 499);
        }
        return valueString;
    }
}
