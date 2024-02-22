package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.TableGenerationField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface GenTableFieldMapper extends MyBatisPlusMapper<TableGenerationField> {

    List<TableGenerationField> getByTableId(Long tableId);

    int deleteBatchTableIds(Long[] tableIds);
}
