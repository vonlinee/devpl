package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.BaseDao;
import io.devpl.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 */
@Mapper
public interface TableDao extends BaseDao<GenTable> {

}
