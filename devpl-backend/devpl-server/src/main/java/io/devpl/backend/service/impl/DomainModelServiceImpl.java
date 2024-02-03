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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 模型信息管理
 */
@Service
public class DomainModelServiceImpl extends ServiceImpl<ModelInfoMapper, ModelInfo> implements DomainModelService {

    @Resource
    FieldInfoService fieldInfoService;
    @Resource
    ModelInfoMapper modelInfoMapper;

    @Override
    public ModelInfo getModelInfo(Long modelId) {
        ModelInfo modelInfo = getById(modelId);
        if (modelInfo != null) {
            modelInfo.setFields(baseMapper.selectModelFields(modelId));
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
    public boolean removeField(Long modelId, Collection<Long> fieldIds) {
        return modelInfoMapper.removeFields(modelId, fieldIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateModel(ModelInfo modelInfo) {
        updateById(modelInfo);
        // 已有的字段列表
        final List<Long> fieldIds = baseMapper.selectModelFieldIds(modelInfo.getId());
        // 直接覆盖 存在则新增，不存在则删除
        if (!CollectionUtils.isEmpty(modelInfo.getFields())) {
            Set<Long> paramFieldIds = CollectionUtils.toSet(modelInfo.getFields(), ModelField::getFieldId);
            // 删除的字段
            List<Long> filedIdsToRemove = CollectionUtils.filter(fieldIds, id -> !paramFieldIds.contains(id));
            // 字段已被删除，删除模型字段关联
            filedIdsToRemove.addAll(fieldInfoService.filterExisted(paramFieldIds));

            if (!CollectionUtils.isEmpty(filedIdsToRemove)) {
                modelInfoMapper.removeFields(modelInfo.getId(), filedIdsToRemove);
            }
            // 新增的字段
            List<ModelField> fieldsToAdd = CollectionUtils.filter(modelInfo.getFields(), f -> !fieldIds.contains(f.getFieldId()));
            if (!CollectionUtils.isEmpty(fieldsToAdd)) {
                this.addFieldsForModel(modelInfo.getId(), CollectionUtils.toSet(modelInfo.getFields(), ModelField::getId));
            }
        }
        return true;
    }

    @Override
    public boolean addFieldsForModel(Long modelId, Collection<Long> fieldIds) {
        return baseMapper.insertModeFieldRelation(modelId, fieldIds) > 0;
    }

    @Override
    public boolean save(ModelInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        return super.save(entity);
    }
}
