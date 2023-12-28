package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.GroupField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupFieldMapper extends BaseMapper<GroupField> {

    @Select(value = "SELECT * FROM group_field WHERE group_id = #{groupId}")
    List<Long> selectFieldIdsByGroupId(@Param("groupId") Long groupId);
}
