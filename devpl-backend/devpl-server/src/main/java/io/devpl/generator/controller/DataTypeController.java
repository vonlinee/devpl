package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.DataTypeAddParam;
import io.devpl.generator.domain.param.DataTypeMappingParam;
import io.devpl.generator.domain.vo.DataTypeGroupVO;
import io.devpl.generator.domain.vo.DataTypeMappingListVO;
import io.devpl.generator.domain.vo.DataTypeMappingVO;
import io.devpl.generator.entity.DataTypeGroup;
import io.devpl.generator.entity.DataTypeItem;
import io.devpl.generator.service.IDataTypeService;
import io.devpl.generator.utils.BusinessUtils;
import jakarta.annotation.Resource;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

/**
 * 数据类型管理
 */
@RestController
@RequestMapping("/api/datatype")
public class DataTypeController {

    @Resource
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
     * 更新数据类型信息
     *
     * @return 数据类型信息
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteDataType(Long typeId) {
        return Result.ok(dataTypeService.removeById(typeId));
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
    public Result<Boolean> addDataTypeMapping(@RequestBody DataTypeMappingParam param) {
        return Result.ok(dataTypeService.addDataTypeMapping(param.getTypeId(), param.getAnotherTypeId()));
    }

    /**
     * 查询所有数据类型之间的映射关系
     *
     * @return 类型分组信息
     */
    @GetMapping("/mapping/all")
    public PageResult<DataTypeMappingListVO> listAllDataTypeMappings(DataTypeMappingParam param) {
        return PageResult.ok(dataTypeService.listDataTypeMappings(param.getTypeId()));
    }

    /**
     * 查询某个类型可映射的数据类型
     * 类型映射关系：一对多
     *
     * @param typeId 类型ID 如果为空，则查询所有未设置过类型映射的数据类型，不为空这查询从类型
     * @return 数据类型映射关系列表
     */
    @GetMapping("/mappable")
    public PageResult<DataTypeMappingVO> listAllMappableDataTypes(@Nullable Long typeId) {
        return PageResult.ok(dataTypeService.listAllMappableDataTypes(typeId));
    }
}
