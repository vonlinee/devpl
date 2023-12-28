package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.FieldGroupListParam;
import io.devpl.backend.domain.param.FieldGroupParam;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.backend.entity.GroupField;
import io.devpl.backend.service.FieldGroupService;
import io.devpl.backend.service.FieldInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字段管理控制器
 */
@RestController
@RequestMapping(value = "/api/field")
public class FieldInfoController {

    @Resource
    FieldInfoService fieldInfoService;
    @Resource
    FieldGroupService fieldGroupService;

    /**
     * 分页查询列表
     *
     * @return 列表
     */
    @GetMapping(value = "/page")
    public ListResult<FieldInfo> listPage(FieldInfoListParam param) {
        return ListResult.ok(fieldInfoService.selectPage(param));
    }

    /**
     * 分页查询列表
     *
     * @return 列表
     */
    @GetMapping(value = "/list")
    public Result<List<FieldInfo>> listAll(FieldInfoListParam param) {
        return Result.ok(fieldInfoService.listFields(param));
    }

    /**
     * 保存
     *
     * @return 列表
     */
    @PostMapping(value = "/save")
    public Result<Boolean> save(@RequestBody FieldInfo fieldInfo) {
        return Result.ok(fieldInfoService.saveOrUpdate(fieldInfo));
    }

    /**
     * 批量保存字段
     *
     * @return 列表
     */
    @PostMapping(value = "/save/batch")
    public Result<Boolean> saveBatch(@RequestBody List<FieldInfo> fieldInfo) {
        return Result.ok(fieldInfoService.saveFieldsInfos(fieldInfo));
    }

    /**
     * 删除
     *
     * @return 列表
     */
    @DeleteMapping(value = "/delete")
    public Result<Boolean> delete(@RequestBody List<Long> ids) {
        return Result.ok(fieldInfoService.removeByIds(ids));
    }

    /**
     * 字段解析
     *
     * @return 解析得到的字段信息
     */
    @PostMapping(value = "/parse")
    public Result<List<FieldInfo>> parseFields(@RequestBody FieldParseParam param) {
        return Result.ok(fieldInfoService.parseFields(param));
    }

    /**
     * 字段解析 - 获取示例文本
     *
     * @param type 输入类型
     * @return 解析得到的字段信息
     */
    @GetMapping(value = "/parse/sample")
    public Result<String> getFieldParseSampleText(String type) {
        return Result.ok(fieldInfoService.getSampleText(type));
    }

    /**
     * @return 字段组列表
     */
    @GetMapping(value = "/group/page")
    public ListResult<FieldGroup> pageFieldGroups(FieldGroupListParam param) {
        return ListResult.ok(fieldGroupService.listPage(param));
    }

    /**
     * 新增字段组
     *
     * @return 字段组信息
     */
    @PostMapping(value = "/group/new")
    public Result<FieldGroup> pageFieldGroups() {
        return Result.ok(fieldGroupService.newGroup());
    }

    /**
     * 删除字段组
     *
     * @return 字段组信息
     */
    @DeleteMapping(value = "/group")
    public Result<Boolean> removeGroup(Long id) {
        return Result.ok(fieldGroupService.removeFieldGroupById(id));
    }

    /**
     * 编辑字段组
     *
     * @return 字段组信息
     */
    @PostMapping(value = "/group")
    public Result<Boolean> updateGroup(@RequestBody FieldGroupParam param) {
        return Result.ok(fieldGroupService.updateFieldGroup(param));
    }

    /**
     * 查询字段组包含的字段信息
     *
     * @return 字段组的字段列表
     */
    @GetMapping(value = "/group/field-list")
    public ListResult<GroupField> listGroupFields(FieldGroupListParam param) {
        return ListResult.ok(fieldGroupService.listGroupFieldsById(param.getGroupId()));
    }
}
