package io.devpl.generator.controller;

import io.devpl.generator.common.PageQuery;
import io.devpl.generator.common.page.PageResult;
import io.devpl.generator.common.utils.Result;
import io.devpl.generator.entity.FieldInfo;
import io.devpl.generator.service.FieldInfoService;
import io.devpl.generator.utils.BusinessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/field")
public class FieldInfoController {

    @Autowired
    FieldInfoService fieldInfoService;

    /**
     * 分页查询列表
     * @return 列表
     */
    @GetMapping(value = "/page")
    public Result<PageResult<FieldInfo>> list(PageQuery query) {
        return Result.ok(BusinessUtils.page2List(fieldInfoService.pages(query.getPageIndex(), query.getPageSize())));
    }
}
