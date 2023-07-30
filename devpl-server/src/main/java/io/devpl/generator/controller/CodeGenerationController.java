package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.domain.FileNode;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.CodeGenService;
import io.devpl.generator.service.GeneratorConfigService;
import io.devpl.generator.service.TemplateFileGenerationService;
import io.devpl.sdk.collection.Lists;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final GeneratorConfigService generatorConfigService;

    /**
     * 生成代码（自定义目录）
     * @param tableIds 数据库表ID
     * @return 所有生成的根目录
     */
    @PostMapping("/code")
    public Result<List<String>> generatorCode(@RequestBody Long[] tableIds) {
        // 生成代码
        List<String> rootDirs = new ArrayList<>(tableIds.length);
        for (Long tableId : tableIds) {
            rootDirs.add(codeGenService.startCodeGeneration(tableId));
        }
        return Result.ok(rootDirs);
    }

    /**
     * 生成代码（自定义目录）
     * @return 所有生成的根目录
     */
    @GetMapping("/config")
    public Result<String> getGeneratorConfig() {
        return Result.ok(generatorConfigService.getCodeGenConfigContent());
    }

    /**
     * 生成代码（自定义目录）
     * @return 所有生成的根目录
     */
    @PostMapping("/config")
    public Result<Boolean> saveGeneratorConfig(String content) {
        return Result.ok(generatorConfigService.saveGeneratorConfig(content));
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
     * 修改和新增一条生成的文件类型列表
     * @return 生成的文件列表
     */
    @PostMapping("/genfile")
    public Result<Boolean> saveOrUpdateOne(@RequestBody TemplateFileGeneration param) {
        return Result.ok(templateFileGenerationService.saveOrUpdate(param));
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

    /**
     * 保存或更新生成的文件类型列表
     * 不存在的删除，更新或新增
     * @return 生成的文件列表
     */
    @DeleteMapping("/genfiles/replace")
    public Result<?> deleteGeneratedFileTypes(@RequestBody List<Integer> ids) {
        return Result.ok(templateFileGenerationService.removeBatchByIds(ids));
    }

    /**
     * 获取生成结果的文件树
     * @param rootPath 根路径
     * @return 该目录下的文件列表，树形结构
     */
    @GetMapping("/file-tree")
    public Result<List<FileNode>> get(String rootPath) {
        return Result.ok(codeGenService.getFileTree(rootPath));
    }

    /**
     * 获取文件文本内容
     * @param path 文件路径
     * @return 文本内容
     */
    @GetMapping("/file")
    public Result<String> getFileContent(String path) {
        return Result.ok(codeGenService.getFileContent(path));
    }
}
