package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.ModelInfoMapper;
import io.devpl.backend.domain.param.ModelListParam;
import io.devpl.backend.entity.ModelField;
import io.devpl.backend.entity.ModelInfo;
import io.devpl.backend.service.DomainModelService;
import io.devpl.backend.service.FieldInfoService;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
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
    public ModelInfo getModelInfo(Long modelId) {
        ModelInfo modelInfo = getById(modelId);
        if (modelInfo != null) {
            List<ModelField> modelFields = baseMapper.selectModelFields(modelId);
            modelInfo.setFields(modelFields);
        }
        return modelInfo;
    }

    @Override
    public IPage<ModelInfo> listPage(ModelListParam param) {
        LambdaQueryWrapper<ModelInfo> qw = new LambdaQueryWrapper<>();
        qw.like(ModelInfo::getCode, param.getCode());
        return baseMapper.selectPage(param.asPage(), qw);
    }

    @Override
    public ModelInfo getById(ModelListParam param) {
        ModelInfo modelInfo = getById(param.getId());
        if (modelInfo != null) {
            List<ModelField> modelFields = baseMapper.selectModelFields(modelInfo.getId());
            modelInfo.setFields(modelFields);
        }
        return modelInfo;
    }

    @Override
    public boolean saveModel(ModelInfo modelInfo) {

        return false;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean updateModel(ModelInfo modelInfo) {
        updateById(modelInfo);
        List<Long> fieldIds = baseMapper.selectModelFieldIds(modelInfo.getId());
        if (!CollectionUtils.isEmpty(modelInfo.getFields())) {
            modelInfo.getFields().removeIf(f -> fieldIds.contains(f.getFieldId()));
            if (!CollectionUtils.isEmpty(modelInfo.getFields())) {
                addFieldsForModel(modelInfo.getId(), CollectionUtils.toList(modelInfo.getFields(), ModelField::getId));
            }
        }
        return true;
    }

    @Override
    public boolean addFieldsForModel(Long modelId, List<Long> fieldIds) {
        return baseMapper.insertModeFieldRelation(modelId, fieldIds) > 0;
    }

    @Override
    public boolean save(ModelInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }
}
