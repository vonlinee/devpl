package io.devpl.backend.controller;

import io.devpl.backend.common.PageParam;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
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
    public ListResult<FieldInfo> list(PageParam query) {
        return ListResult.ok(fieldInfoService.selectPage(query.getPageIndex(), query.getPageSize()));
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
