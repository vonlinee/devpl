package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TableGeneration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据表
 */
@Mapper
public interface TableGenerationMapper extends MyBatisPlusMapper<TableGeneration> {

    @Select(value = "SELECT * FROM table_generation WHERE table_name = #{tableName}")
    TableGeneration selectOneByTableName(@Param("tableName") String tableName);

    @Select(value = "SELECT table_name FROM table_generation WHERE datasource_id = #{dataSourceId}")
    List<String> selectTableNames(@Param("dataSourceId") Long dataSourceId);
}
