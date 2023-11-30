package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.BaseClassListParam;
import io.devpl.backend.entity.ModelInfo;
import io.devpl.backend.service.DomainModelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 基类管理
 */
@RestController
@RequestMapping("/api/model")
public class BaseClassController {

    @Resource
    DomainModelService baseClassService;

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 基本类型
     */
    @GetMapping("/list/page")
    public ListResult<ModelInfo> list(BaseClassListParam param) {
        return ListResult.ok(baseClassService.listPage(param));
    }

    @GetMapping("/{id}")
    public Result<ModelInfo> get(@PathVariable("id") Long id) {
        return Result.ok(baseClassService.getById(id));
    }

    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody ModelInfo entity) {
        return Result.ok(baseClassService.save(entity));
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody ModelInfo entity) {
        baseClassService.updateById(entity);
        return Result.ok();
    }

    @DeleteMapping("/remove")
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(baseClassService.removeBatchByIds(Arrays.asList(ids)));
    }
}
