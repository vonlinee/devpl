package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.service.TemplateService;
import io.devpl.generator.utils.BusinessUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 模板管理控制器
 */
@RestController
@RequestMapping(value = "/api/template")
public class TemplateController {

    @Resource
    TemplateService templateService;

    /**
     * 新增模板
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @PostMapping(value = "/save")
    public Result<Boolean> addOne(@RequestBody TemplateInfo templateInfo) {
        return Result.ok(templateService.save(templateInfo));
    }

    /**
     * 新增模板
     * @param templateId 模板ID
     * @return 是否成功
     */
    @PostMapping(value = "/del/{templateId}")
    public Result<Boolean> addOne(@PathVariable(value = "templateId") Integer templateId) {
        return Result.ok(templateService.removeById(templateId));
    }

    /**
     * 分页查询列表
     * @return 列表
     */
    @GetMapping(value = "/page")
    public Result<PageResult<TemplateInfo>> list(PageQuery query) {
        return Result.ok(BusinessUtils.page2List(templateService.pages(query.getPageIndex(), query.getPageSize())));
    }
}
