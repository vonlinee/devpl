package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.MappedStatementItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * MyBatis Mapper语句记录表
 **/
@Mapper
public interface MappedStatementItemMapper extends MyBatisPlusMapper<MappedStatementItem> {

    @Select("SELECT DISTINCT belong_file FROM mapped_statement_item")
    Set<String> listBelongedFiles();
}
