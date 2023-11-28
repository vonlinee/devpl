package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.Query;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.entity.GenFieldType;
import io.devpl.backend.service.GenFieldTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;

/**
 * 字段类型管理
 */
@RestController
@RequestMapping("/gen/fieldtype")
public class FieldTypeController {

    @Resource
    GenFieldTypeService fieldTypeService;

    /**
     * 字段类型
     *
     * @param query 查询参数
     * @return GenFieldType
     */
    @GetMapping("page")
    public ListResult<GenFieldType> page(Query query) {
        return fieldTypeService.page(query);
    }

    @GetMapping("{id}")
    public Result<GenFieldType> get(@PathVariable("id") Long id) {
        return Result.ok(fieldTypeService.getById(id));
    }

    @GetMapping("list")
    public Result<Set<String>> list() {
        return Result.ok(fieldTypeService.getList());
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody GenFieldType entity) {
        return Result.ok(fieldTypeService.save(entity));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody GenFieldType entity) {
        return Result.ok(fieldTypeService.updateById(entity));
    }

    @DeleteMapping
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(fieldTypeService.removeBatchByIds(Arrays.asList(ids)));
    }
}
