package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.DataTypeAddParam;
import io.devpl.backend.domain.param.DataTypeGroupParam;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingParam;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.service.DataTypeItemService;
import jakarta.annotation.Resource;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据类型管理
 */
@RestController
@RequestMapping("/api/datatype")
public class DataTypeController {

    @Resource
    DataTypeItemService dataTypeService;

    /**
     * 保存数据类型信息
     *
     * @return 数据类型信息
     */
    @PostMapping("/edit")
    public Result<Boolean> editDataType(@RequestBody DataTypeItem param) {
        return Result.ok(dataTypeService.saveOrUpdate(param));
    }

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
    public ListResult<DataTypeItem> listDataTypes(DataTypeListParam param) {
        return ListResult.ok(dataTypeService.selectPage(param));
    }

    /**
     * 查询数据类型分组
     *
     * @return 类型分组信息
     */
    @GetMapping("/groups")
    public ListResult<DataTypeGroupVO> addDataTypeGroup() {
        return ListResult.ok(dataTypeService.listDataTypeGroups());
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
     * 删除数据类型分组
     *
     * @return 类型分组信息
     */
    @DeleteMapping("/group/remove")
    public boolean removeDataTypeGroups(@RequestBody DataTypeGroupParam param) {
        return dataTypeService.removeDataTypeGroupByIds(param);
    }

    /**
     * 保存数据类型分组
     *
     * @return 类型分组信息
     */
    @PostMapping("/group/saveupdate/batch")
    public Result<Boolean> saveOrUpdateTypeGroups(@RequestBody List<DataTypeGroup> groups) {
        return Result.ok(dataTypeService.saveOrUpdateTypeGroups(groups));
    }

    /**
     * 添加数据类型映射关系
     *
     * @return 类型分组信息
     */
    @PostMapping("/mapping")
    public Result<Boolean> addDataTypeMapping(@RequestBody DataTypeMappingParam param) {
        return Result.ok(dataTypeService.addDataTypeMapping(param.getMappings()));
    }

    /**
     * 查询所有数据类型之间的映射关系
     *
     * @return 类型分组信息
     */
    @GetMapping("/mapping/all")
    public ListResult<DataTypeMappingListVO> listAllDataTypeMappings(DataTypeMappingParam param) {
        return ListResult.ok(dataTypeService.listDataTypeMappings(param.getTypeId()));
    }

    /**
     * 查询某个类型可映射的数据类型
     * 类型映射关系：一对多
     *
     * @param typeId 类型ID 如果为空，则查询所有未设置过类型映射的数据类型，不为空这查询从类型
     * @return 数据类型映射关系列表
     */
    @GetMapping("/mappable")
    public ListResult<DataTypeMappingVO> listAllMappableDataTypes(@Nullable Long typeId) {
        return ListResult.ok(dataTypeService.listAllMappableDataTypes(typeId));
    }

    /**
     * 获取某个分组可选择的类型列表，包含名称和ID
     *
     * @param typeGroupId 类型分组，为空则获取所有
     * @return 选项VO
     */
    @GetMapping("/options")
    public Result<List<SelectOptionVO>> getSelectableTypes(
        @RequestParam(name = "typeGroupId") String typeGroupId) {
        return Result.ok(dataTypeService.getSelectableTypes(typeGroupId));
    }

    /**
     * @return 选项VO
     */
    @GetMapping("/group/options")
    public Result<List<SelectOptionVO>> getSelectableTypeGroups() {
        return Result.ok(dataTypeService.getSelectableTypeGroups());
    }
}
