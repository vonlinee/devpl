package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.DbConnInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 */
@Mapper
public interface DbConnInfoMapper extends EntityMapper<DbConnInfo> {

}