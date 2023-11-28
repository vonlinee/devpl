package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.ProjectListParam;
import io.devpl.backend.domain.vo.ProjectSelectVO;
import io.devpl.backend.entity.ProjectInfo;
import io.devpl.backend.service.ProjectService;
import io.devpl.backend.utils.ServletUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ListResult<ProjectInfo> page(ProjectListParam param) {
        return projectService.listProjectInfos(param);
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
        entity.setUpdateTime(LocalDateTime.now());
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
