package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.MappedStatementItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis Mapper语句记录表
 **/
@Mapper
public interface MappedStatementItemMapper extends MyBatisPlusMapper<MappedStatementItem> {

}
