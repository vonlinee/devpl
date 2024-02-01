package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateVariableMetadataMapper;
import io.devpl.backend.entity.TemplateVariableMetadata;
import io.devpl.backend.service.TemplateVariableMetadataService;
import org.springframework.stereotype.Service;

@Service
public class TemplateVariableMetadataServiceImpl extends ServiceImpl<TemplateVariableMetadataMapper, TemplateVariableMetadata> implements TemplateVariableMetadataService {

}
