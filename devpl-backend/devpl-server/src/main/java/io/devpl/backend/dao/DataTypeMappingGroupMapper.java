package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.DataTypeMappingGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @see io.devpl.backend.entity.DataTypeMapping
 */
@Mapper
public interface DataTypeMappingGroupMapper extends BaseMapper<DataTypeMappingGroup> {

    /**
     * 查询所有类型映射组
     *
     * @return 类型映射组列表
     */
    @Select(value = "SELECT * FROM data_type_mapping_group")
    List<DataTypeMappingGroup> listAll();
}
