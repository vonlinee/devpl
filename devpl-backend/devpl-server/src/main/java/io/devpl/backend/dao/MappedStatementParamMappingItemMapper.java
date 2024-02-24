package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.MappedStatementParamMappingItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自定义指令Mapper
 */
@Mapper
public interface MappedStatementParamMappingItemMapper extends MyBatisPlusMapper<MappedStatementParamMappingItem> {

    @Override
    default Class<MappedStatementParamMappingItem> getEntityClass() {
        return MappedStatementParamMappingItem.class;
    }
}
