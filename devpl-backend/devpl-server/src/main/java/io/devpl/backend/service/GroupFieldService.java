package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.enums.CrudMode;
import io.devpl.backend.entity.GroupField;

import java.util.Collection;
import java.util.List;

public interface GroupFieldService extends IService<GroupField> {

    List<Long> listFieldIdByGroupId(Long groupId);

    List<GroupField> listByGroupId(Long groupId);

    /**
     * 更新字段关联关系
     *
     * @param groupId  分组ID
     * @param fieldIds 字段ID列表
     * @param crudMode 操作模式
     * @return 是否成功
     */
    boolean updateGroupFieldRelation(Long groupId, Collection<Long> fieldIds, CrudMode crudMode);
}
