package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateFileGenerationMapper;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TemplateFileGeneration;
import io.devpl.backend.service.TemplateArgumentService;
import io.devpl.backend.service.TemplateFileGenerationService;
import io.devpl.common.utils.JSONUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TemplateFileGenerationServiceImpl extends ServiceImpl<TemplateFileGenerationMapper, TemplateFileGeneration> implements TemplateFileGenerationService {

    @Resource
    TemplateArgumentService argumentService;

    /**
     * 保存模板文件的模板参数
     *
     * @param generation 表生成信息
     * @param arguments  单个模板参数
     */
    @Override
    public boolean saveTemplateFileGenerationArguments(TableFileGeneration generation, Map<String, Object> arguments) {
//        List<TemplateArgument> list = argumentService.initialize(generation.getTemplateId(), generation.getGenerationId(), arguments);
//        argumentService.saveBatch(list);
        TemplateFileGeneration tfg = getById(generation.getGenerationId());
        if (tfg != null) {
            tfg.setTemplateArguments(JSONUtils.toString(arguments));
        }
        return updateById(tfg);
    }
}
