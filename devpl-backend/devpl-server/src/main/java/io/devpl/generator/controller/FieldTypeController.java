package io.devpl.generator.controller;

import io.devpl.generator.common.query.PageResult;
import io.devpl.generator.common.query.Query;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.entity.GenFieldType;
import io.devpl.generator.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;

/**
 * 字段类型管理
 */
@RestController
@RequestMapping("/gen/fieldtype")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;

    /**
     * 字段类型
     * @param query 查询参数
     * @return GenFieldType
     */
    @GetMapping("page")
    public Result<PageResult<GenFieldType>> page(Query query) {
        return Result.ok(fieldTypeService.page(query));
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
