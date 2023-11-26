package io.devpl.generator.controller;

import io.devpl.generator.common.query.ListResult;
import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.param.Query;
import io.devpl.generator.domain.vo.ProjectSelectVO;
import io.devpl.generator.entity.ProjectInfo;
import io.devpl.generator.service.ProjectService;
import io.devpl.generator.utils.ServletUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目名变更
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Resource
    ProjectService projectService;

    @GetMapping("/selectable")
    public Result<List<ProjectSelectVO>> listSelectableProjects() {
        return Result.ok(projectService.listSelectableProject());
    }

    @GetMapping("/page")
    public ListResult<ProjectInfo> page(@Valid Query query) {
        return projectService.listProjectInfos(query);
    }

    @GetMapping("/{id}")
    public Result<ProjectInfo> get(@PathVariable("id") Long id) {
        return Result.ok(projectService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody ProjectInfo entity) {
        return Result.ok(projectService.save(entity));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid ProjectInfo entity) {
        return Result.ok(projectService.updateById(entity));
    }

    @DeleteMapping
    public Result<Boolean> delete(@RequestBody List<Long> idList) {
        return Result.ok(projectService.removeByIds(idList));
    }

    /**
     * 源码下载
     */
    @GetMapping("download/{id}")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        ProjectInfo project = projectService.getById(id);
        byte[] data = projectService.download(project);
        ServletUtils.downloadFile(response, project.getModifyProjectName() + ".zip", data);
    }
}
