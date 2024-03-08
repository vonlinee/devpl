package io.devpl.backend.service.impl;

import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.dao.TableGenerationFieldMapper;
import io.devpl.backend.domain.enums.AutoFillEnum;
import io.devpl.backend.domain.enums.FormType;
import io.devpl.backend.entity.GenFieldType;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.entity.TableGenerationField;
import io.devpl.backend.service.TableGenerationFieldService;
import io.devpl.codegen.core.CaseFormat;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表字段
 */
@Service
public class TableGenerationFieldServiceImpl extends MyBatisPlusServiceImpl<TableGenerationFieldMapper, TableGenerationField> implements TableGenerationFieldService {

    @Override
    public List<TableGenerationField> listByTableId(Long tableId) {
        return baseMapper.getByTableId(tableId);
    }

    @Override
    public boolean deleteBatchTableIds(Long[] tableIds) {
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

    @Override
    public List<TableGenerationField> initTableFields(TableGeneration table, List<TableGenerationField> tableFieldList) {
        // 字段类型、属性类型映射
        Map<String, GenFieldType> fieldTypeMap = new HashMap<>();
        int index = 0;
        for (TableGenerationField field : tableFieldList) {

            // 关联表和字段
            field.setTableId(table.getId());

            field.setAttrName(CaseFormat.toCamelCase(field.getFieldName()));
            // 获取字段对应的类型
            GenFieldType fieldTypeMapping = fieldTypeMap.get(field.getFieldType().toLowerCase());
            if (fieldTypeMapping == null) {
                // 没找到对应的类型，则为Object类型
                field.setAttrType("Object");
            } else {
                field.setAttrType(fieldTypeMapping.getAttrType());
                field.setPackageName(fieldTypeMapping.getPackageName());
            }

            field.setAutoFill(AutoFillEnum.DEFAULT.name());
            field.setFormItem(true);
            field.setGridItem(true);
            field.setQueryType("=");
            field.setQueryFormType("text");
            field.setFormType(FormType.TEXT.getText());
            field.setSort(index++);
        }
        return tableFieldList;
    }
}
