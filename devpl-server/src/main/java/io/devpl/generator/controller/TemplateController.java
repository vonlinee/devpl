package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.service.TemplateService;
import io.devpl.generator.utils.BusinessUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模板管理控制器
 */
@RestController
@RequestMapping(value = "/api/template")
public class TemplateController {

    @Resource
    TemplateService templateService;

    /**
     * 分页查询列表
     * @return 列表
     */
    @GetMapping(value = "/page")
    public Result<PageResult<TemplateInfo>> list(PageQuery query) {
        return Result.ok(BusinessUtils.page2List(templateService.pages(query.getPageIndex(), query.getPageSize())));
    }
}
