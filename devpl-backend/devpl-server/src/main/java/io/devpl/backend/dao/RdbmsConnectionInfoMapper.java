package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源管理
 */
@Mapper
public interface RdbmsConnectionInfoMapper extends MyBatisPlusMapper<RdbmsConnectionInfo> {

}
