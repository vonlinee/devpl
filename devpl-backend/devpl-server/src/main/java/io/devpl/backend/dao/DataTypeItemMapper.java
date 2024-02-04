package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.DataTypeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据类型表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-10-12
 **/
@Mapper
public interface DataTypeItemMapper extends MyBatisPlusMapper<DataTypeItem> {

    List<DataTypeItem> listByGroupId(@Param("typeGroupId") String typeGroup);
}
