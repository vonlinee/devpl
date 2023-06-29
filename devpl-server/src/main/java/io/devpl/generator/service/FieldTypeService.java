package io.devpl.generator.service;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.FieldTypeEntity;

import java.util.Map;
import java.util.Set;

/**
 * 字段类型管理
 */
public interface FieldTypeService extends BaseService<FieldTypeEntity> {
    PageResult<FieldTypeEntity> page(Query query);

    Map<String, FieldTypeEntity> getMap();

    /**
     * 根据tableId，获取包列表
     * @param tableId 表ID
     * @return 返回包列表
     */
    Set<String> getPackageByTableId(Long tableId);

    Set<String> getList();
}
