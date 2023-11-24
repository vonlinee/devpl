package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.generator.dao.TemplateVarInfoMapper;
import io.devpl.generator.entity.TemplateVarInfo;
import io.devpl.generator.service.TemplateVarInfoService;
import org.springframework.stereotype.Service;

@Service
public class TemplateVarInfoServiceImpl extends ServiceImpl<TemplateVarInfoMapper, TemplateVarInfo> implements TemplateVarInfoService {

}
