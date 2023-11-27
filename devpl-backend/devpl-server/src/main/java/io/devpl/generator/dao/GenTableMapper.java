package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据表
 */
@Mapper
public interface GenTableMapper extends EntityMapper<GenTable> {

    @Select(value = "SELECT * FROM gen_table WHERE table_name = #{tableName}")
    GenTable selectOneByTableName(@Param("tableName") String tableName);

    @Select(value = "SELECT table_name FROM gen_table WHERE datasource_id = #{dataSourceId}")
    List<String> selectTableNames(@Param("dataSourceId") Long dataSourceId);
}
