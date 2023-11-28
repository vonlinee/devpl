package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.EntityMapper;
import io.devpl.backend.entity.DataTypeItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据类型表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-10-12
 **/
@Mapper
public interface DataTypeItemMapper extends EntityMapper<DataTypeItem> {

}