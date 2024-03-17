package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.param.FileGenerationParam;
import io.devpl.backend.domain.param.JavaPojoCodeGenParam;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.domain.vo.FileGenerationResult;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TargetGenerationFile;
import io.devpl.backend.service.*;
import io.devpl.common.model.FileNode;
import io.devpl.sdk.validation.Assert;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 代码生成器
 */
@RestController
@RequestMapping("/api/codegen")
@AllArgsConstructor
public class CodeGenerationController {

    FileGenerationService fileGenService;
    TargetGenerationFileService targetGenFileService;
    TableFileGenerationService tableFileGenerationService;
    GeneratorConfigService generatorConfigService;
    CodeGenerationService codeGenerationService;

    /**
     * 生成代码
     *
     * @param param 文件生成参数
     * @return 所有生成的根目录
     */
    @PostMapping("/file")
    public FileGenerationResult generatorCode(@RequestBody FileGenerationParam param) {
        return fileGenService.generateFile(param);
    }

    /**
     * 生成代码（自定义目录）
     *
     * @return 所有生成的根目录
     */
    @GetMapping("/config")
    public String getGeneratorConfig() {
        return generatorConfigService.getCodeGenConfigContent();
    }

    /**
     * 生成代码（自定义目录）
     *
     * @return 所有生成的根目录
     */
    @PostMapping("/config")
    public boolean saveGeneratorConfig(String content) {
        return generatorConfigService.saveGeneratorConfig(content);
    }

    /**
     * 生成的文件类型列表
     *
     * @return 生成的文件列表
     */
    @GetMapping("/genfiles")
    public Result<List<TargetGenerationFile>> listGeneratedFileTypes() {
        return Result.ok(targetGenFileService.listGeneratedFileTypes());
    }

    /**
     * 生成的文件类型列表
     *
     * @return 生成的文件列表
     */
    @PostMapping("/genfiles")
    public Result<Boolean> updateGeneratedFileTypes(@RequestBody List<TargetGenerationFile> param) {
        return Result.ok(targetGenFileService.saveOrUpdateBatch(param));
    }

    /**
     * 修改和新增一条生成的文件类型列表
     *
     * @return 生成的文件列表
     */
    @PostMapping("/genfile")
    public boolean saveOrUpdateOne(@RequestBody TargetGenerationFile param) {
        return targetGenFileService.saveOrUpdate(param);
    }

    /**
     * 保存或更新生成的文件类型列表
     * 不存在的删除，更新或新增
     *
     * @return 生成的文件列表
     */
    @PostMapping("/genfiles/replace")
    public Result<Boolean> saveOrUpdateGeneratedFileTypes(@RequestBody List<TargetGenerationFile> files) {
        return Result.ok(targetGenFileService.saveOrUpdateBatch(files));
    }

    /**
     * 获取单个表需要生成哪些文件
     *
     * @param tableId 需要生活的表ID
     * @return 文件列表
     */
    @GetMapping("/genfiles/list")
    public Result<List<TableFileGeneration>> listFilesToBeGenerated(@RequestParam(name = "tableId") Long tableId) {
        return Result.ok(tableFileGenerationService.listByTableId(tableId));
    }

    /**
     * 修改单个表需要生成的文件信息
     *
     * @param param 单个表需要生成的文件信息
     * @return 文件列表
     */
    @PostMapping("/genfiles/config")
    public Result<Boolean> updateFilesToBeGenerated(@RequestBody TableFileGenParam param) {
        return Result.ok(tableFileGenerationService.updateFilesToBeGenerated(param));
    }

    /**
     * 删除单个表需要生成的文件信息
     *
     * @param param 单个表需要生成的文件信息
     * @return 文件列表
     */
    @DeleteMapping("/genfiles/remove")
    public Result<Boolean> removeFilesToBeGenerated(@RequestBody TableFileGenParam param) {
        if (param.isLr()) {
            for (TableFileGeneration tfg : param.getFileInfoList()) {
                tfg.setDeleted(true);
            }
            return Result.ok(tableFileGenerationService.updateFilesToBeGenerated(param));
        }
        return Result.ok(tableFileGenerationService.removeBatchByIds(param.getFileInfoList()));
    }

    /**
     * 保存或更新生成的文件类型列表
     * 不存在的删除，更新或新增
     *
     * @return 生成的文件列表
     */
    @DeleteMapping("/genfiles/replace")
    public Result<?> deleteGeneratedFileTypes(@RequestBody List<Integer> ids) {
        ids.removeIf(Objects::isNull);
        Assert.notEmpty(ids, "参数为空");
        return Result.ok(targetGenFileService.removeBatchByIds(ids));
    }

    /**
     * 获取生成结果的文件树
     *
     * @param rootPath 根路径
     * @return 该目录下的文件列表，树形结构
     */
    @GetMapping("/file-tree")
    public Result<List<FileNode>> get(String rootPath) {
        return Result.ok(fileGenService.getGeneratedFileTree(rootPath));
    }

    /**
     * 获取文件文本内容
     *
     * @param path 文件路径
     * @return 文本内容
     */
    @GetMapping("/file")
    public Result<String> getFileContent(String path) {
        return Result.ok(fileGenService.getFileContent(path));
    }

    /**
     * 生成JavaPojo
     *
     * @param param 参数
     * @return 生成结果
     */
    @PostMapping("/java/pojo")
    public Result<String> generateJavaPojoClass(@RequestBody JavaPojoCodeGenParam param) {
        if (param.getType() == 3) {
            return Result.ok(codeGenerationService.generatePoiPojo(param));
        } else if (param.getType() == 2) {
            return Result.ok(codeGenerationService.generatedDtoClass(param));
        }
        return Result.ok(codeGenerationService.generateJavaPojoClass(param));
    }
}
