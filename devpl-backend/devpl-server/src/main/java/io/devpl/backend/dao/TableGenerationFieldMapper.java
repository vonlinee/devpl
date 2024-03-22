package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TableGenerationField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface TableGenerationFieldMapper extends MyBatisPlusMapper<TableGenerationField> {

    /**
     * 根据 table id 查询里欸包
     *
     * @param tableId 表ID
     * @return 字段列表
     */
    @Select(value = """
        select *
        from table_generation_field
        where table_id = #{tableId}
        order by sort
        """)
    List<TableGenerationField> selectListByTableId(@Param("tableId") Long tableId);

    /**
     * 根据 table id 批量删除
     *
     * @param tableIds 表ID列表
     * @return 是否成功
     */
    int deleteBatchTableIds(Long[] tableIds);
}
