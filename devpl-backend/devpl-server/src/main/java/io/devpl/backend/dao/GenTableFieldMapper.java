package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.GenTableField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface GenTableFieldMapper extends MyBatisPlusMapper<GenTableField> {

    List<GenTableField> getByTableId(Long tableId);

    int deleteBatchTableIds(Long[] tableIds);
}
