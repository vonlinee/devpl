package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.entity.GenTableField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface GenTableFieldMapper extends EntityMapper<GenTableField> {

    List<GenTableField> getByTableId(Long tableId);

    int deleteBatchTableIds(Long[] tableIds);
}
