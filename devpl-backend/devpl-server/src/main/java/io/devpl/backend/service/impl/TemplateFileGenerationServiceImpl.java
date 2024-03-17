package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateFileGenerationMapper;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TemplateArgument;
import io.devpl.backend.entity.TemplateFileGeneration;
import io.devpl.backend.service.TemplateArgumentService;
import io.devpl.backend.service.TemplateFileGenerationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TemplateFileGenerationServiceImpl extends ServiceImpl<TemplateFileGenerationMapper, TemplateFileGeneration> implements TemplateFileGenerationService {

    @Resource
    TemplateArgumentService argumentService;

    @Override
    public void generate(Long id) {

    }

    @Override
    public void saveTemplateArguments(TableFileGeneration generation, Map<String, Object> arguments) {
        List<TemplateArgument> list = argumentService.initialize(generation.getTemplateId(), generation.getGenerationId(), arguments);
        argumentService.saveBatch(list);
    }
}
