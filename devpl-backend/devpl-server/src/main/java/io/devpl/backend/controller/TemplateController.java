package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.CustomTemplateDirectiveParam;
import io.devpl.backend.domain.param.TemplateInfoListParam;
import io.devpl.backend.domain.param.TemplateParamParam;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.domain.vo.TemplateProviderVO;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.CustomDirective;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.entity.TemplateParam;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.backend.service.TemplateDirectiveService;
import io.devpl.backend.service.TemplateParamService;
import io.devpl.backend.service.TemplateService;
import io.devpl.codegen.type.SimpleDataType;
import io.devpl.sdk.validation.Assert;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Resource
    TemplateParamService templateParamService;
    @Resource
    TemplateDirectiveService templateDirectiveService;
    @Resource
    DataTypeItemService dataTypeService;

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
     * 获取模板类型
     *
     * @return 是否成功
     */
    @GetMapping(value = "/types")
    public Result<List<TemplateProviderVO>> listTemplateTYpes() {
        return Result.ok(templateService.listTemplateTypes());
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
    public ListResult<TemplateInfo> list(TemplateInfoListParam param) {
        return ListResult.ok(templateService.listPageTemplates(param));
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

    /**
     * 根据ID获取模板信息
     *
     * @return 列表
     */
    @GetMapping(value = "/param/list")
    public ListResult<TemplateParam> listTemplateParams(@RequestParam(value = "templateId", required = false) Long templateId) {
        return ListResult.ok(templateParamService.listTemplateParamsByTemplateId(templateId));
    }

    /**
     * 保存模板参数信息
     *
     * @return 列表
     */
    @PostMapping(value = "/param")
    public boolean listTemplateParams(@RequestBody TemplateParamParam params) {
        return templateParamService.saveOrUpdateBatch(params.getParams());
    }

    /**
     * 保存模板参数信息
     *
     * @return 列表
     */
    @GetMapping(value = "/param/datatypes")
    public List<SelectOptionVO> listTemplateParamDatatype() {
        List<SelectOptionVO> result = new ArrayList<>();
        for (SimpleDataType item : SimpleDataType.values()) {
            result.add(new SelectOptionVO(item.getQualifier(), item.getQualifier(), item.getQualifier()));
        }
        return result;
    }

    /**
     * 获取自定义指令的示例文本
     *
     * @return 列表
     */
    @GetMapping(value = "/directive/custom/example")
    public String getCustomTemplateDirectiveExample() {
        return """
            public class CustomTemplateDirective implements TemplateDirective {

                /**
                 * 指令名称，该指令在模板中的名称
                 *
                 * @return 指令名称
                 */
                @Override
                public String getName() {
                    return "指令名称";
                }

                @Override
                public Class<?>[] getParameterTypes() {
                    return new Class[0];
                }

                @Override
                public String render(Object[] params) {
                    return "渲染结果";
                }
            }
            """;
    }

    /**
     * 获取自定义指令列表
     *
     * @return 列表
     */
    @GetMapping(value = "/directive/custom/list")
    public List<CustomDirective> listCustomTemplateDirective(CustomTemplateDirectiveParam param) {
        return templateDirectiveService.listCustomDirectives(param);
    }

    /**
     * 添加自定义指令
     *
     * @return 列表
     */
    @PostMapping(value = "/directive/custom/add")
    public boolean addCustomTemplateDirective(@RequestBody CustomTemplateDirectiveParam param) {
        return templateDirectiveService.addCustomDirective(param);
    }

    /**
     * 删除自定义指令
     *
     * @return 列表
     */
    @DeleteMapping(value = "/directive/custom/remove")
    public boolean removeCustomTemplateDirective(@RequestBody CustomTemplateDirectiveParam param) {
        return templateDirectiveService.removeCustomDirective(param);
    }
}
