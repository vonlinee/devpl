package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.GenTableField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 表字段
 */
@Mapper
public interface TableFieldDao extends BaseMapper<GenTableField> {

    List<GenTableField> getByTableId(Long tableId);

    void deleteBatchTableIds(Long[] tableIds);
}
