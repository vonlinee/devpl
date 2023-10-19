package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.DataTypeAddParam;
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
    @PostMapping("/add")
    public Result<Boolean> addDataTypes(@RequestBody DataTypeAddParam param) {
        return Result.ok(dataTypeService.saveDataTypes(param.getDataTypeItems()));
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
    @PostMapping("/group/add")
    public Result<Boolean> addDataTypeGroup(@RequestBody DataTypeGroup param) {
        return Result.ok(dataTypeService.saveDataTypeGroup(param));
    }
}