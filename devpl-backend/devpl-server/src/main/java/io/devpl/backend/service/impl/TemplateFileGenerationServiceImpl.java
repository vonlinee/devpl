package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateFileGenerationMapper;
import io.devpl.backend.entity.TemplateFileGeneration;
import io.devpl.backend.service.TemplateFileGenerationService;
import org.springframework.stereotype.Service;

@Service
public class TemplateFileGenerationServiceImpl extends ServiceImpl<TemplateFileGenerationMapper, TemplateFileGeneration> implements TemplateFileGenerationService {

    @Override
    public void generate(Long id) {

    }
}
