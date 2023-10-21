package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.DataTypeAddParam;
import io.devpl.generator.domain.param.DataTypeMappingAddParam;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.service.IDataTypeService;
import io.devpl.generator.utils.BusinessUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 数据类型管理
 */
@RestController
@RequestMapping("/api/datatype")
@AllArgsConstructor
public class DataTypeController {

    IDataTypeService dataTypeService;

    /**
     * 保存数据类型信息
     *
     * @return 数据类型信息
     */
    @PostMapping("/saveOrUpdateBatch")
    public Result<Boolean> addDataTypes(@RequestBody DataTypeAddParam param) {
        return Result.ok(dataTypeService.saveDataTypes(param.getDataTypeItems()));
    }

    /**
     * 更新数据类型信息
     *
     * @return 数据类型信息
     */
    @PostMapping("/update")
    public Result<Boolean> updateDataType(@RequestBody DataTypeItem param) {
        return Result.ok(dataTypeService.update(param));
    }

    /**
     * 查询数据类型信息
     *
     * @return 数据类型信息
     */
    @GetMapping("/page")
    public Result<PageResult<DataTypeItem>> listDataTypes(PageQuery param) {
        return Result.ok(BusinessUtils.page2List(dataTypeService.selectPage(param)));
    }

    /**
     * 保存数据类型分组
     *
     * @return 类型分组信息
     */
    @GetMapping("/groups")
    public PageResult<DataTypeGroupVO> addDataTypeGroup() {
        return PageResult.ok(dataTypeService.listDataTypeGroups());
    }

    /**
     * 保存数据类型分组
     *
     * @return 类型分组信息
     */
    @PostMapping("/group/add")
    public Result<Boolean> addDataTypeGroup(@RequestBody DataTypeGroup param) {
        return Result.ok(dataTypeService.saveDataTypeGroup(param));
    }

    /**
     * 添加数据类型映射关系
     *
     * @return 类型分组信息
     */
    @PostMapping("/mapping")
    public Result<Boolean> addDataTypeMapping(@RequestBody DataTypeMappingAddParam param) {
        return Result.ok(dataTypeService.addDataTypeMapping(param.getTypeId(), param.getAnotherTypeId()));
    }
}
