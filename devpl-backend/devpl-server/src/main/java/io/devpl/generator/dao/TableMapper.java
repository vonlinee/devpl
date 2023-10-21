package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 */
@Mapper
public interface TableMapper extends EntityMapper<GenTable> {

}
