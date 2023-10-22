package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
import io.devpl.generator.utils.BusinessUtils;
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
    public Result<PageResult<FieldInfo>> list(PageQuery query) {
        return Result.ok(BusinessUtils.page2List(fieldInfoService.pages(query.getPageIndex(), query.getPageSize())));
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
     * 删除
     *
     * @return 列表
     */
    @DeleteMapping(value = "/delete")
    public Result<Boolean> delete(@RequestBody List<FieldInfo> fieldInfoList) {
        return Result.ok(fieldInfoService.saveOrUpdateBatch(fieldInfoList));
    }
}
