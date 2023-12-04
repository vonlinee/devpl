package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.entity.FieldInfo;
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
}
