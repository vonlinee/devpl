package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.ModelInfoMapper;
import io.devpl.backend.domain.param.BaseClassListParam;
import io.devpl.backend.entity.ModelField;
import io.devpl.backend.entity.ModelInfo;
import io.devpl.backend.service.DomainModelService;
import io.devpl.backend.service.FieldInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 模型信息管理
 */
@Service
public class DomainModelServiceImpl extends ServiceImpl<ModelInfoMapper, ModelInfo> implements DomainModelService {

    @Resource
    FieldInfoService fieldInfoService;

    @Override
    public IPage<ModelInfo> listPage(BaseClassListParam param) {
        LambdaQueryWrapper<ModelInfo> qw = new LambdaQueryWrapper<>();
        qw.like(ModelInfo::getCode, param.getCode());
        return baseMapper.selectPage(param.asPage(), qw);
    }

    @Override
    public ModelInfo getById(BaseClassListParam param) {
        ModelInfo modelInfo = getById(param.getId());
        if (modelInfo != null) {
            List<ModelField> modelFields = baseMapper.selectModelFields(modelInfo.getId());
            modelInfo.setFields(modelFields);
        }
        return modelInfo;
    }

    @Override
    public boolean save(ModelInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }
}
