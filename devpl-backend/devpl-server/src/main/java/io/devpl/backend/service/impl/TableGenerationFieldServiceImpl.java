package io.devpl.backend.service.impl;

import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.dao.TableGenerationFieldMapper;
import io.devpl.backend.entity.TableGenerationField;
import io.devpl.backend.service.TableGenerationFieldService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表字段
 */
@Service
public class TableGenerationFieldServiceImpl extends MyBatisPlusServiceImpl<TableGenerationFieldMapper, TableGenerationField> implements TableGenerationFieldService {

    /**
     * 根据 table id 查询列表
     *
     * @param tableId 表ID
     * @return 字段列表
     */
    @Override
    public List<TableGenerationField> listByTableId(Long tableId) {
        return baseMapper.selectListByTableId(tableId);
    }

    /**
     * 根据 table id 批量删除
     *
     * @param tableIds 表ID列表
     * @return 是否成功
     */
    @Override
    public boolean deleteBatchByTableIds(Long[] tableIds) {
        return baseMapper.deleteBatchTableIds(tableIds) > 0;
    }

    @Override
    public void updateTableField(Long tableId, List<TableGenerationField> tableFieldList) {
        // 更新字段数据
        int sort = 0;
        for (TableGenerationField tableField : tableFieldList) {
            tableField.setSort(sort++);
            this.updateById(tableField);
        }
    }
}
