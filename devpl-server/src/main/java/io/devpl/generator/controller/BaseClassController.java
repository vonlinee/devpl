package io.devpl.generator.controller;

import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.entity.GenBaseClass;
import io.devpl.generator.service.BaseClassService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 基类管理
 */
@RestController
@RequestMapping("/gen/baseclass")
@AllArgsConstructor
public class BaseClassController {
    private final BaseClassService baseClassService;

    /**
     * 分页查询
     * @param query 查询参数
     * @return 基本类型
     */
    @GetMapping("/page")
    public Result<PageResult<GenBaseClass>> page(Query query) {
        return Result.ok(baseClassService.page(query));
    }

    @GetMapping("/list")
    public Result<List<GenBaseClass>> list() {
        return Result.ok(baseClassService.getList());
    }

    @GetMapping("/{id}")
    public Result<GenBaseClass> get(@PathVariable("id") Long id) {
        return Result.ok(baseClassService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody GenBaseClass entity) {
        return Result.ok(baseClassService.save(entity));
    }

    @PutMapping
    public Result<String> update(@RequestBody GenBaseClass entity) {
        baseClassService.updateById(entity);
        return Result.ok();
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        baseClassService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok();
    }
}
