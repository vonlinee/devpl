package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.generator.dao.TemplateFileGenerationMapper;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.TemplateFileGenerationService;
import org.springframework.stereotype.Service;

@Service
public class TemplateFileGenerationServiceImpl extends ServiceImpl<TemplateFileGenerationMapper, TemplateFileGeneration> implements TemplateFileGenerationService {

    @Override
    public void generate(Long id) {

    }
}
