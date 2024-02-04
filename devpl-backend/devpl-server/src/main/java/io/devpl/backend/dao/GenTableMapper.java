package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据表
 */
@Mapper
public interface GenTableMapper extends MyBatisPlusMapper<GenTable> {

    @Select(value = "SELECT * FROM gen_table WHERE table_name = #{tableName}")
    GenTable selectOneByTableName(@Param("tableName") String tableName);

    @Select(value = "SELECT table_name FROM gen_table WHERE datasource_id = #{dataSourceId}")
    List<String> selectTableNames(@Param("dataSourceId") Long dataSourceId);
}
