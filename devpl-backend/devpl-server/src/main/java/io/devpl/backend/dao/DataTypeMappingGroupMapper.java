package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.entity.DataTypeMappingGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @see io.devpl.backend.entity.DataTypeMapping
 */
@Mapper
public interface DataTypeMappingGroupMapper extends BaseMapper<DataTypeMappingGroup> {

}
