package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.GroupField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mapper
public interface FieldGroupMapper extends BaseMapper<FieldGroup> {

    /**
     * 查询组的字段列表
     *
     * @param groupId 字段组ID
     * @return 组的字段列表
     */
    List<GroupField> selectGroupFieldsById(Long groupId);

    /**
     * 获取字段组的最大ID
     *
     * @return 字段组的最大ID
     */
    @Nullable
    @Select(value = "SELECT MAX(id) FROM field_group")
    Long getMaxGroupId();
}