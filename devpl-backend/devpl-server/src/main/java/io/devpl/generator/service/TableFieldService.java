package io.devpl.generator.service;

import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.GenTableField;

import java.util.List;

/**
 * 表字段
 */
public interface TableFieldService extends BaseService<GenTableField> {

    /**
     * 查询表的所有字段
     * @param tableId 表ID
     * @return 表的字段
     */
    List<GenTableField> listByTableId(Long tableId);

    boolean deleteBatchTableIds(Long[] tableIds);

    /**
     * 修改表字段数据
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    void updateTableField(Long tableId, List<GenTableField> tableFieldList);

    /**
     * 初始化字段数据
     */
    void initFieldList(List<GenTableField> tableFieldList);
}
