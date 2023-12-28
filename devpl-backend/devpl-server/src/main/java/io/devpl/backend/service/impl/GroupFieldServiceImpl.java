package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.GroupFieldMapper;
import io.devpl.backend.entity.GroupField;
import io.devpl.backend.service.GroupFieldService;
import io.devpl.sdk.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class GroupFieldServiceImpl extends ServiceImpl<GroupFieldMapper, GroupField> implements GroupFieldService {

    @Override
    public List<Long> listFieldIdByGroupId(Long groupId) {
        return baseMapper.selectFieldIdsByGroupId(groupId);
    }

    @Override
    public List<GroupField> listByGroupId(Long groupId) {
        LambdaQueryWrapper<GroupField> qw = new LambdaQueryWrapper<>();
        qw.eq(GroupField::getGroupId, groupId);
        return list(qw);
    }

    @Override
    public boolean addGroupFieldRelation(Long groupId, Collection<Long> fieldIds) {
        List<Long> existedFieldIds = listFieldIdByGroupId(groupId);
        CollectionUtils.removeAll(fieldIds, existedFieldIds);
        return saveBatch(fieldIds.stream().map(fieldId -> new GroupField(groupId, fieldId)).toList());
    }
}
