package io.devpl.generator.service.impl;

import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.dao.TemplateFileGenerationDao;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.TemplateFileGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Service
@AllArgsConstructor
public class TemplateFileGenerationServiceImpl extends BaseServiceImpl<TemplateFileGenerationDao, TemplateFileGeneration> implements TemplateFileGenerationService {

    @Override
    public List<TemplateFileGeneration> listGeneratedFileTypes() {
        return baseMapper.selectGeneratedFileTypes();
    }
}
