package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.GroupField;

import java.util.Collection;
import java.util.List;

public interface GroupFieldService extends IService<GroupField> {

    List<Long> listFieldIdByGroupId(Long groupId);

    List<GroupField> listByGroupId(Long groupId);

    boolean addGroupFieldRelation(Long groupId, Collection<Long> fieldIds);
}
