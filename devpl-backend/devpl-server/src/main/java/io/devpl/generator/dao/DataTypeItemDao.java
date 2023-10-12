package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.BaseDao;
import io.devpl.generator.entity.DataTypeItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据类型表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-10-12
 **/
@Mapper
public interface DataTypeItemDao extends BaseDao<DataTypeItem> {

}
