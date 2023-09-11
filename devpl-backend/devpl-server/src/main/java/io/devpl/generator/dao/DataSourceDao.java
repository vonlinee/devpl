package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.GenDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 */
@Mapper
public interface DataSourceDao extends BaseMapper<GenDataSource> {

}