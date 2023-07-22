package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.common.utils.ServletUtils;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.CodeGenService;
import io.devpl.generator.service.TemplateFileGenerationService;
import io.devpl.sdk.collection.Lists;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成控制器
 */
@Controller
@RequestMapping("/gen/generator")
@AllArgsConstructor
public class CodeGenerationController {
    private final CodeGenService codeGenService;
    private final TemplateFileGenerationService templateFileGenerationService;

    /**
     * 生成代码（zip压缩包）
     */
    @GetMapping("/download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            // 生成代码
            for (String tableId : tableIds.split(",")) {
                codeGenService.downloadCode(Long.parseLong(tableId), zip);
            }
            // zip压缩包数据
            ServletUtils.downloadFile(response, "devpl.zip", outputStream.toByteArray());
        }
    }

    /**
     * 生成代码（自定义目录）
     * @param tableIds 数据库表ID
     * @return 所有生成的根目录
     */
    @ResponseBody
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
    @ResponseBody
    public Result<List<TemplateFileGeneration>> listGeneratedFileTypes() {
        return Result.ok(templateFileGenerationService.listGeneratedFileTypes());
    }
}
