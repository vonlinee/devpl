package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.Model2DDLParam;
import io.devpl.backend.service.DevToolsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发工具控制器
 */
@RestController
@RequestMapping("/api/devtools")
public class DevToolsController {

    @Resource
    DevToolsService devToolsService;

    /**
     * 实体类转DDL
     *
     * @param param 参数
     * @return DDL
     */
    @PostMapping(value = "/ddl")
    public Result<String> model2Ddl(@RequestBody Model2DDLParam param) {
        return Result.ok(devToolsService.model2DDL(param));
    }
}
