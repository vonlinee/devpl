package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.ModelListParam;
import io.devpl.backend.domain.param.ModelUpdateParam;
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
public class DomainModelController {

    @Resource
    DomainModelService modelService;

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 基本类型
     */
    @GetMapping("/list/page")
    public ListResult<ModelInfo> listPage(ModelListParam param) {
        return ListResult.ok(modelService.listPage(param));
    }

    /**
     * 获取模型信息
     *
     * @param id 模型ID
     * @return 模型信息
     */
    @GetMapping("/{id}")
    public Result<ModelInfo> get(@PathVariable("id") Long id) {
        return Result.ok(modelService.getModelInfo(id));
    }

    /**
     * 保存模型信息
     *
     * @param entity 模型信息
     * @return 是否成功
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody ModelInfo entity) {
        return Result.ok(modelService.saveModel(entity));
    }

    /**
     * 更新模型信息
     *
     * @param entity 模型信息
     * @return 是否成功
     */
    @PutMapping("/update")
    public boolean update(@RequestBody ModelInfo entity) {
        return modelService.updateModel(entity);
    }

    /**
     * 删除模型字段
     *
     * @param param ModelUpdateParam
     * @return 是否成功
     */
    @DeleteMapping("/field/remove")
    public boolean removeModelField(@RequestBody ModelUpdateParam param) {
        return modelService.removeField(param.getModelId(), param.getFieldIds());
    }

    @DeleteMapping("/remove")
    public Result<Boolean> delete(@RequestBody Long[] ids) {
        return Result.ok(modelService.removeBatchByIds(Arrays.asList(ids)));
    }
}
