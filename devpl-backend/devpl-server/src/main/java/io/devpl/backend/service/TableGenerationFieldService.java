package io.devpl.backend.service;

import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.entity.TableGenerationField;

import java.util.List;

/**
 * 表字段
 */
public interface TableGenerationFieldService extends BaseService<TableGenerationField> {

    /**
     * 查询表的所有字段
     *
     * @param tableId 表ID
     * @return 表的字段
     */
    List<TableGenerationField> listByTableId(Long tableId);

    boolean deleteBatchTableIds(Long[] tableIds);

    /**
     * 修改表字段数据
     *
     * @param tableId        表ID
     * @param tableFieldList 字段列表
     */
    void updateTableField(Long tableId, List<TableGenerationField> tableFieldList);

    /**
     * 初始化字段数据
     */
    List<TableGenerationField> initTableFields(List<TableGenerationField> tableFieldList);
}
