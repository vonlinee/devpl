package io.devpl.generator.service.impl;

import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.dao.TemplateFileGenerationMapper;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.TemplateFileGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 模板文件生成关联表
 */
@Service
@AllArgsConstructor
public class TemplateFileGenerationServiceImpl extends BaseServiceImpl<TemplateFileGenerationMapper, TemplateFileGeneration> implements TemplateFileGenerationService {

}
