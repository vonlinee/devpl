package io.devpl.backend.controller;

import io.devpl.backend.common.PageParam;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.service.TemplateService;
import io.devpl.sdk.validation.Assert;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模板管理控制器
 */
@RestController
@RequestMapping(value = "/api/codegen/template")
public class TemplateController {

    @Resource
    TemplateService templateService;

    /**
     * 模板上传
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @PostMapping(value = "/upload")
    public Result<Boolean> uploadTemplate(TemplateInfo templateInfo) {
        Assert.notNull(templateInfo.getType(), "模板类型为空");
        Assert.isTrue(templateInfo.getType() == 1 || templateInfo.getType() == 2, "模板类型参数错误");
        if (templateInfo.isFileTemplate()) {
            Assert.hasText(templateInfo.getTemplatePath(), "文件路径不能为空");
        } else {
            Assert.hasText(templateInfo.getContent(), "模板内容不能为空");
        }
        return Result.ok(templateService.addTemplate(templateInfo));
    }

    /**
     * 新增模板
     * 模板内容以字符串进行传输
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @PostMapping(value = "/persist")
    public Result<Boolean> saveOrUpdateById(@RequestBody TemplateInfo templateInfo) {
        return Result.ok(templateInfo.getTemplateId() == null ? templateService.addTemplate(templateInfo) : templateService.updateById(templateInfo));
    }

    /**
     * 根据ID更新
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @PutMapping(value = "/update")
    public Result<Boolean> update(@RequestBody TemplateInfo templateInfo) {
        Assert.notNull(templateInfo.getTemplateId(), "模板ID为空");
        Assert.notNull(templateInfo.getType(), "模板类型为空");
        Assert.isTrue(templateInfo.getType() == 1 || templateInfo.getType() == 2, "模板类型参数错误");
        return Result.ok(templateService.updateById(templateInfo));
    }

    /**
     * 根据ID删除模板
     *
     * @param id 模板ID
     * @return 是否成功
     */
    @DeleteMapping(value = "/delete/{id}")
    public Result<Boolean> removeById(@PathVariable(value = "id") Integer id) {
        return Result.ok(templateService.removeById(id));
    }

    /**
     * 根据ID批量删除模板
     *
     * @param templateIds 模板ID
     * @return 是否成功
     */
    @DeleteMapping(value = "/delete/batch/ids")
    public Result<Boolean> removeBatchByIds(@RequestBody List<Integer> templateIds) {
        Assert.notEmpty(templateIds, "参数为空");
        Assert.notEmpty(templateIds.stream().filter(Objects::nonNull).collect(Collectors.toList()), "参数为空");
        return Result.ok(templateService.removeByIds(templateIds));
    }

    /**
     * 分页查询列表
     *
     * @return 列表
     */
    @GetMapping(value = "/page")
    public ListResult<TemplateInfo> list(PageParam query) {
        return ListResult.ok(templateService.listPageTemplates(query.getPageIndex(), query.getPageSize()));
    }

    /**
     * 可选择的模板列表
     *
     * @return 列表
     */
    @GetMapping(value = "/list/selectable")
    public ListResult<TemplateSelectVO> listSelectableTemplates() {
        return ListResult.ok(templateService.listSelectable());
    }

    /**
     * 根据ID获取模板信息
     *
     * @return 列表
     */
    @GetMapping(value = "/info/{templateId}")
    public Result<TemplateInfo> getTemplateById(@PathVariable(value = "templateId") Integer templateId) {
        return Result.ok(templateService.getById(templateId));
    }
}
