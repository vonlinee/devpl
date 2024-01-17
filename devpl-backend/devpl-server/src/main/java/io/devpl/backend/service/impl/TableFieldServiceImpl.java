package io.devpl.backend.service.impl;

import io.devpl.backend.common.mvc.BaseServiceImpl;
import io.devpl.backend.dao.GenTableFieldMapper;
import io.devpl.backend.domain.enums.AutoFillEnum;
import io.devpl.backend.domain.enums.FormType;
import io.devpl.backend.entity.GenFieldType;
import io.devpl.backend.entity.GenTableField;
import io.devpl.backend.service.GenFieldTypeService;
import io.devpl.backend.service.GenTableFieldService;
import io.devpl.codegen.core.CaseFormat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 表字段
 */
@Service
@AllArgsConstructor
public class TableFieldServiceImpl extends BaseServiceImpl<GenTableFieldMapper, GenTableField> implements GenTableFieldService {
    private final GenFieldTypeService fieldTypeService;

    @Override
    public List<GenTableField> listByTableId(Long tableId) {
        return baseMapper.getByTableId(tableId);
    }

    @Override
    public boolean deleteBatchTableIds(Long[] tableIds) {
        return baseMapper.deleteBatchTableIds(tableIds) > 0;
    }

    @Override
    public void updateTableField(Long tableId, List<GenTableField> tableFieldList) {
        // 更新字段数据
        int sort = 0;
        for (GenTableField tableField : tableFieldList) {
            tableField.setSort(sort++);
            this.updateById(tableField);
        }
    }

    @Override
    public void initFieldList(List<GenTableField> tableFieldList) {
        // 字段类型、属性类型映射
        Map<String, GenFieldType> fieldTypeMap = fieldTypeService.getMap();
        int index = 0;
        for (GenTableField field : tableFieldList) {
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
    }
}
