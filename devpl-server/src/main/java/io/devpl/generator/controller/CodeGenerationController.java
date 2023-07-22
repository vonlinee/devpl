package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.CodeGenService;
import io.devpl.generator.service.TemplateFileGenerationService;
import io.devpl.sdk.collection.Lists;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成控制器
 */
@RestController
@RequestMapping("/gen/generator")
@AllArgsConstructor
public class CodeGenerationController {
    private final CodeGenService codeGenService;
    private final TemplateFileGenerationService templateFileGenerationService;

    /**
     * 生成代码（自定义目录）
     * @param tableIds 数据库表ID
     * @return 所有生成的根目录
     */
    @PostMapping("/code")
    public Result<List<String>> generatorCode(@RequestBody Long[] tableIds) throws Exception {
        // 生成代码
        for (Long tableId : tableIds) {
            codeGenService.generatorCode(tableId);
        }
        GeneratorInfo generatorInfo = codeGenService.getGeneratorInfo();
        return Result.ok(Lists.arrayOf(generatorInfo.getProject().getBackendPath(), generatorInfo.getProject()
            .getFrontendPath()));
    }

    /**
     * 生成的文件类型列表
     * @return 生成的文件列表
     */
    @GetMapping("/genfiles")
    public Result<List<TemplateFileGeneration>> listGeneratedFileTypes() {
        return Result.ok(templateFileGenerationService.listGeneratedFileTypes());
    }

    /**
     * 保存或更新生成的文件类型列表
     * 不存在的删除，更新或新增
     * @return 生成的文件列表
     */
    @PostMapping("/genfiles/replace")
    public Result<?> saveOrUpdateGeneratedFileTypes(@RequestBody List<TemplateFileGeneration> files) {
        return Result.ok(templateFileGenerationService.saveOrUpdateBatch(files));
    }
}
