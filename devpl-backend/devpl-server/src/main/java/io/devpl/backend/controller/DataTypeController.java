package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.*;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMappingGroup;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.backend.service.DataTypeMappingService;
import io.devpl.backend.utils.BusinessUtils;
import io.devpl.sdk.util.CollectionUtils;
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
    @Resource
    DataTypeMappingService dataTypeMappingService;

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
        return Result.ok(dataTypeService.saveOrUpdateBatch(param.getDataTypeItems()));
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
     * 分页查询所有数据类型之间的映射关系
     * 查询主数据类型
     *
     * @return 类型分组信息
     */
    @GetMapping("/mapping/list")
    public ListResult<DataTypeMappingListVO> listAllDataTypeMappings(DataTypeMappingListParam param) {
        // TODO 查询某个类型 被哪些类型映射
        return ListResult.ok(BusinessUtils.startPageInfo(param, p -> dataTypeService.listDataTypeMappings(param)));
    }

    /**
     * 根据ID删除
     *
     * @return 是否成功
     */
    @DeleteMapping("/mapping/remove")
    public boolean removeMappingById(@RequestParam("id") Long id) {
        return dataTypeMappingService.removeById(id);
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
     * 数据类型组列表
     *
     * @return 选项VO
     */
    @GetMapping("/group/options")
    public Result<List<SelectOptionVO>> getSelectableTypeGroups(String excludeTypeGroupId) {
        return Result.ok(dataTypeService.getSelectableTypeGroups(excludeTypeGroupId));
    }

    /**
     * 数据类型映射分组列表
     *
     * @return 选项VO
     */
    @GetMapping("/mapping/group/options")
    public Result<List<SelectOptionVO>> listSelectableTypeMappingGroups() {
        return Result.ok(dataTypeMappingService.listMappingGroupOptions());
    }

    /**
     * 新增数据类型映射分组
     *
     * @return 是否成功
     */
    @PostMapping("/mapping/group/add")
    public Result<Boolean> addDataTypeMappingGroup(@RequestBody DataTypeMappingGroup group) {
        return Result.ok(dataTypeMappingService.addTypeMappingGroup(group));
    }

    /**
     * 数据类型映射分组列表
     *
     * @return 选项VO
     */
    @PostMapping("/mapping/add")
    public Result<Boolean> addDataTypeMapping(@RequestBody DataTypeMappingAddParam param) {
        if (param.getGroupId() == null) {
            return Result.error("类型映射规则分组为空");
        }
        if (param.getTypeId() == null || CollectionUtils.isEmpty(param.getAnotherTypeIds())) {
            return Result.error("主类型或者映射类型为空");
        }
        return Result.ok(dataTypeService.addDataTypeMapping(param));
    }

    /**
     * 查询没有映射过的数据类型选项列表
     *
     * @return 选项VO
     */
    @GetMapping("/mapping/primary/options")
    public ListResult<DataTypeItem> listSelectablePrimaryTypeOptions(DataTypeListParam param) {
        return ListResult.ok(dataTypeMappingService.listSelectablePrimaryTypes(param));
    }

    /**
     * 查询某个类型可以被添加映射关系的其他数据类型
     * 和该类型不属于同一个类型分组，且去除已经添加过该类型的映射关系的数据类型
     *
     * @return 选项VO
     */
    @GetMapping("/mapping/another/options")
    public ListResult<DataTypeItem> listSelectableAnotherTypeOptions(DataTypeListParam param) {
        DataTypeItem dataType = dataTypeService.getById(param.getExcludeTypeId());
        if (dataType == null) {
            return ListResult.error("数据类型不存在");
        }
        param.setExcludeTypeGroupId(dataType.getTypeGroupId());
        return ListResult.ok(dataTypeMappingService.listSelectableAnotherTypes(param));
    }
}
