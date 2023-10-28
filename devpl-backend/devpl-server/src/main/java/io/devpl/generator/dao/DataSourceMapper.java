package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.DbConnInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 */
@Mapper
public interface DataSourceMapper extends EntityMapper<DbConnInfo> {

}
