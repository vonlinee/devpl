package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.GroupFieldMapper;
import io.devpl.backend.domain.enums.CrudMode;
import io.devpl.backend.entity.GroupField;
import io.devpl.backend.service.GroupFieldService;
import io.devpl.sdk.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroupFieldRelation(Long groupId, Collection<Long> fieldIds, CrudMode mode) {
        // 该组已有的字段
        List<Long> existedFieldIds = listFieldIdByGroupId(groupId);
        // 要新增的
        Set<Long> idsToInsert = CollectionUtils.removeAll(new HashSet<>(fieldIds), existedFieldIds);
        if (!CollectionUtils.isEmpty(idsToInsert)) {
            saveBatch(idsToInsert.stream().map(fieldId -> new GroupField(groupId, fieldId)).toList());
        }
        // 要删除的
        HashSet<Long> idsToBeDelete = CollectionUtils.removeAll(new HashSet<>(existedFieldIds), fieldIds);
        if (!CollectionUtils.isEmpty(idsToBeDelete)) {
            removeBatchByIds(idsToBeDelete);
        }
        return true;
    }
}
