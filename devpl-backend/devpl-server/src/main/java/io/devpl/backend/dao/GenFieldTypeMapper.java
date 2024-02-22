package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.GenFieldType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 字段类型管理
 */
@Mapper
public interface GenFieldTypeMapper extends MyBatisPlusMapper<GenFieldType> {

    /**
     * 根据tableId，获取包列表
     */
    @Select(value = """
            select t1.package_name
            from gen_field_type t1,
                 table_generation_field t2
            where t1.attr_type = t2.attr_type
              and t2.table_id = #{tableId}
            """)
    Set<String> getPackageByTableId(@Param("tableId") Long tableId);

    /**
     * 获取全部字段类型
     */
    @Select(value = "select DISTINCT attr_type from gen_field_type")
    Set<String> list();
}
