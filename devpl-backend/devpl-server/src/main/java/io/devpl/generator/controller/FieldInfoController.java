package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.FieldParseParam;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
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
    public ListResult<FieldInfo> list(PageQuery query) {
        return ListResult.ok(fieldInfoService.pages(query.getPageIndex(), query.getPageSize()));
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

    /**
     * 字段解析
     *
     * @return
     */
    @PostMapping(value = "/parse")
    public ListResult<FieldInfo> parseFields(@RequestBody FieldParseParam param) {
        List<FieldInfo> fieldInfoList = fieldInfoService.parseFields(param);

        return ListResult.ok(fieldInfoList);
    }
}
