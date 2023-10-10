package io.devpl.generator.dao;

import io.devpl.generator.common.dao.BaseDao;
import io.devpl.generator.entity.GenFieldType;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 字段类型管理
 */
@Mapper
public interface FieldTypeDao extends BaseDao<GenFieldType> {

    /**
     * 根据tableId，获取包列表
     */
    Set<String> getPackageByTableId(Long tableId);

    /**
     * 获取全部字段类型
     */
    Set<String> list();
}
