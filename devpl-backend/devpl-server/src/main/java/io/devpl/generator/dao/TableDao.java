package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据表
 */
@Mapper
public interface TableDao extends BaseMapper<GenTable> {

}
