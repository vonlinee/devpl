package io.devpl.backend.service;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.entity.GenFieldType;

import java.util.Map;
import java.util.Set;

/**
 * 字段类型管理
 */
public interface GenFieldTypeService extends BaseService<GenFieldType> {
    ListResult<GenFieldType> page(Query query);

    Map<String, GenFieldType> getMap();

    /**
     * 根据tableId，获取包列表
     * @param tableId 表ID
     * @return 返回包列表
     */
    Set<String> getPackageByTableId(Long tableId);

    Set<String> getList();
}
