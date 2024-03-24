package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.Model2DDLParam;
import io.devpl.backend.domain.param.TableCreatorParam;
import io.devpl.backend.domain.vo.ColumnInfoVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.GroupField;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.backend.service.DevToolsService;
import io.devpl.backend.service.FieldGroupService;
import io.devpl.codegen.core.CaseFormat;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 开发工具控制器
 */
@RestController
@RequestMapping("/api/devtools")
public class DevToolsController {

    @Resource
    DevToolsService devToolsService;
    @Resource
    FieldGroupService fieldGroupService;
    @Resource
    DataTypeItemService dataTypeService;

    /**
     * TODO 实体类直接转DDL
     *
     * @param param 参数
     * @return DDL
     */
    @PostMapping(value = "/ddl")
    public Result<String> model2Ddl(@RequestBody Model2DDLParam param) {
        return Result.ok(devToolsService.model2DDL(param));
    }

    /**
     * 实体类转DDL
     *
     * @param param 参数
     * @return DDL
     */
    @PostMapping(value = "/table/create/columns")
    public Result<List<ColumnInfoVO>> getCreateTableColumns(@RequestBody TableCreatorParam param) {

        List<GroupField> groupFields = fieldGroupService.listGroupFieldsById(param.getGroupId());
        List<ColumnInfoVO> columns = new ArrayList<>();

        List<SelectOptionVO> dataTypes = dataTypeService.getSelectableTypes("MySQL");

        Set<String> dataTypeLabels = dataTypes.stream().map(vo -> String.valueOf(vo.getKey()).toLowerCase()).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        for (GroupField groupField : groupFields) {
            ColumnInfoVO column = new ColumnInfoVO();
            column.setColumnName(CaseFormat.camelToUnderline(groupField.getFieldName()));
            column.setColumnSize(50);
            column.setDataType("varchar");
            column.setNullable(true);
            column.setRemarks(groupField.getComment());
            columns.add(column);

            if ("id".equals(column.getColumnName())) {
                column.setPrimaryKey(true);
                column.setDataType("bigint");
            }
        }

        if (!CollectionUtils.isEmpty(columns)) {
            columns.get(0).setDataTypes(dataTypeLabels);
        }
        return Result.ok(columns);
    }

    /**
     * 通过列信息生成DDL语句
     *
     * @param param 参数
     * @return DDL
     */
    @PostMapping(value = "/table/create/ddl")
    public String getCreateTableDDL(@RequestBody TableCreatorParam param) {
        return devToolsService.getCreateTableDDL(param);
    }
}
