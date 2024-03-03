package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TableGenerationField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface TableGenerationFieldMapper extends MyBatisPlusMapper<TableGenerationField> {

    @Select(value = """
            select *
            from table_generation_field
            where table_id = #{value}
            order by sort
            """)
    List<TableGenerationField> getByTableId(Long tableId);

    int deleteBatchTableIds(Long[] tableIds);
}
