package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.entity.GenBaseClass;
import io.devpl.backend.service.BaseClassService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 基类管理
 */
@RestController
@RequestMapping("/gen/baseclass")
public class BaseClassController {

    @Resource
    BaseClassService baseClassService;

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 基本类型
     */
    @GetMapping("/page")
    public ListResult<GenBaseClass> page(Query query) {
        return baseClassService.listPage(query);
    }

    @GetMapping("/list")
    public ListResult<GenBaseClass> list() {
        return ListResult.ok(baseClassService.listAll());
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
