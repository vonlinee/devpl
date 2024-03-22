package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.DataTypeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 数据类型表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-10-12
 **/
@Mapper
public interface DataTypeItemMapper extends MyBatisPlusMapper<DataTypeItem> {

    @Override
    default Class<DataTypeItem> getEntityClass() {
        return DataTypeItem.class;
    }

    /**
     * 查询数据类型列表
     *
     * @param dataTypeItem 类型分组ID
     * @return 数据类型列表
     */
    List<DataTypeItem> listByCondition(@Param("param") DataTypeItem dataTypeItem);

    /**
     * 查询单个组内所有的数据类型
     *
     * @param typeGroupId 类型分组ID MySQL忽略大小写
     * @return 数据类型列表
     */
    List<DataTypeItem> listByGroupId(@Param("typeGroupId") String typeGroupId);

    /**
     * 根据主键ID列表查询
     *
     * @param ids 主键ID列表
     * @return 数据类型列表，全部字段
     */
    List<DataTypeItem> listByIds(@Param("ids") Collection<Long> ids);
}
