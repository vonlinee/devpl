package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.entity.CustomDirective;
import io.devpl.backend.entity.DataTypeGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataTypeGroupMapper extends MyBatisPlusMapper<DataTypeGroup> {

    @Override
    default Class<DataTypeGroup> getEntityClass() {
        return DataTypeGroup.class;
    }

    List<DataTypeGroupVO> selectAllGroups();

    @Select(value = "SELECT id FROM data_type_group WHERE group_id = #{groupId}")
    DataTypeGroup selectByGroupId(String groupId);
}
