package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.entity.DataTypeGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataTypeGroupMapper extends BaseMapper<DataTypeGroup> {

    List<DataTypeGroupVO> selectAllGroups();

    @Select("SELECT id FROM data_type_group WHERE group_id = #{groupId}")
    DataTypeGroup selectByGroupId(String groupId);
}
