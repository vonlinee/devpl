package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateVarInfoMapper;
import io.devpl.backend.entity.TemplateVarInfo;
import io.devpl.backend.service.TemplateVarInfoService;
import org.springframework.stereotype.Service;

@Service
public class TemplateVarInfoServiceImpl extends ServiceImpl<TemplateVarInfoMapper, TemplateVarInfo> implements TemplateVarInfoService {

}
