package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.RdbmsConnectionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据源管理
 */
@Mapper
public interface RdbmsConnectionInfoMapper extends MyBatisPlusMapper<RdbmsConnectionInfo> {

    @Select(value = "select * from rdbms_connection_info ORDER BY id")
    List<RdbmsConnectionInfo> selectDataSources();

    @Select(value = "select * from rdbms_connection_info WHERE id = #{dataSourceId} LIMIT 1")
    RdbmsConnectionInfo getByDataSourceId(@Param("dataSourceId") long dataSourceId);
}
