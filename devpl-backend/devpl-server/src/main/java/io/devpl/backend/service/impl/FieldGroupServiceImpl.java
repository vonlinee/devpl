package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.dao.FieldGroupMapper;
import io.devpl.backend.domain.enums.CrudMode;
import io.devpl.backend.domain.param.FieldGroupListParam;
import io.devpl.backend.domain.param.FieldGroupParam;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.entity.GroupField;
import io.devpl.backend.service.FieldGroupService;
import io.devpl.backend.service.FieldInfoService;
import io.devpl.backend.service.GroupFieldService;
import io.devpl.sdk.util.CollectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FieldGroupServiceImpl implements FieldGroupService {

    FieldGroupMapper fieldGroupMapper;
    FieldInfoService fieldInfoService;
    GroupFieldService groupFieldService;

    @Override
    public List<FieldGroup> listFieldGroups(FieldGroupListParam param) {
        LambdaQueryWrapper<FieldGroup> qw = new LambdaQueryWrapper<>();
        return fieldGroupMapper.selectList(qw);
    }

    @Override
    public IPage<FieldGroup> listPage(FieldGroupListParam param) {
        LambdaQueryWrapper<FieldGroup> qw = new LambdaQueryWrapper<>();
        return fieldGroupMapper.selectPage(param.asPage(), qw);
    }

    @Override
    public List<GroupField> listGroupFieldsById(Long groupId) {
        return fieldGroupMapper.selectGroupFieldsById(groupId);
    }

    @Override
    public FieldGroup newGroup() {
        FieldGroup fieldGroup = new FieldGroup();
        Long maxGroupId = fieldGroupMapper.getMaxGroupId();
        if (maxGroupId == null) {
            maxGroupId = 1L;
        }
        fieldGroup.setGroupName("组" + (maxGroupId + 1));
        return fieldGroupMapper.insert(fieldGroup) > 0 ? fieldGroup : null;
    }

    @Override
    public boolean removeFieldGroupById(Long id) {
        return fieldGroupMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFieldGroup(FieldGroupParam param) {
        fieldGroupMapper.updateById(param.getGroup());
        groupFieldService.updateGroupFieldRelation(param.getGroup().getId(), CollectionUtils.toSet(param.getFields(), FieldInfo::getId), CrudMode.OVERWRITE);
        return false;
    }
}
