package io.devpl.backend.service.impl;

import io.devpl.backend.common.mvc.MyBatisPlusServiceImpl;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.dao.TableGenerationFieldMapper;
import io.devpl.backend.domain.enums.AutoFillEnum;
import io.devpl.backend.domain.enums.FormType;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.entity.TableGenerationField;
import io.devpl.backend.service.TableGenerationFieldService;
import io.devpl.codegen.core.CaseFormat;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表字段
 */
@Service
public class TableGenerationFieldServiceImpl extends MyBatisPlusServiceImpl<TableGenerationFieldMapper, TableGenerationField> implements TableGenerationFieldService {

    @Resource
    DataTypeMappingMapper dataTypeMappingMapper;

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

    /**
     * 初始化表字段信息
     *
     * @param table          表信息
     * @param tableFieldList 表字段列表
     * @return {@link List}<{@link TableGenerationField}>
     */
    @Override
    public List<TableGenerationField> initTableFields(TableGeneration table, List<TableGenerationField> tableFieldList) {
        // 字段类型、属性类型映射
        List<DataTypeMapping> mappings = dataTypeMappingMapper.selectListByTypeGroupId("MySQL", "JAVA");

        int index = 0;
        for (TableGenerationField field : tableFieldList) {

            // 关联表和字段
            field.setTableId(table.getId());

            field.setAttrName(CaseFormat.toCamelCase(field.getFieldName()));
            // 获取字段对应的类型

            field.setAttrType(mappingSqlTypeToJavaType(field.getFieldType()));

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

    /**
     * TODO  暂时写死 对接数据类型映射关系
     *
     * @param sqlType sql数据类型
     * @return
     */
    private String mappingSqlTypeToJavaType(String sqlType) {
        String javaType = "String";
        if ("varchar".equalsIgnoreCase(sqlType)) {
            javaType = "String";
        } else if ("int".equalsIgnoreCase(sqlType)) {
            javaType = "Integer";
        } else if ("bigint".equalsIgnoreCase(sqlType)) {
            javaType = "Long";
        } else if ("datetime".equalsIgnoreCase(sqlType)) {
            javaType = "LocalDateTime";
        }
        return javaType;
    }
}
