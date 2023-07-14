package io.devpl.generator.service.impl;

import cn.hutool.core.text.NamingCase;
import io.devpl.generator.common.service.impl.BaseServiceImpl;
import io.devpl.generator.dao.TableFieldDao;
import io.devpl.generator.entity.GenFieldType;
import io.devpl.generator.entity.GenTableField;
import io.devpl.generator.enums.AutoFillEnum;
import io.devpl.generator.service.FieldTypeService;
import io.devpl.generator.service.TableFieldService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 表字段
 */
@Service
@AllArgsConstructor
public class TableFieldServiceImpl extends BaseServiceImpl<TableFieldDao, GenTableField> implements TableFieldService {
    private final FieldTypeService fieldTypeService;

    @Override
    public List<GenTableField> listByTableId(Long tableId) {
        return baseMapper.getByTableId(tableId);
    }

    @Override
    public void deleteBatchTableIds(Long[] tableIds) {
        baseMapper.deleteBatchTableIds(tableIds);
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
            field.setAttrName(NamingCase.toCamelCase(field.getFieldName()));
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
            field.setFormType("text");
            field.setSort(index++);
        }
    }
}
