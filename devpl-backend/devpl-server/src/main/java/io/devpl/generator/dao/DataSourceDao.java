package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.BaseDao;
import io.devpl.generator.entity.DataSourceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 */
@Mapper
public interface DataSourceDao extends BaseDao<DataSourceInfo> {

}
