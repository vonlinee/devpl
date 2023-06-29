package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.service.CodeGenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/factory")
public class FactoryController {

    private final CodeGenService generatorService;

    @GetMapping("/generator/file-tree")
    public Result<?> get(String rootPath) {
        return Result.ok(generatorService.getFileTree(rootPath));
    }

    /**
     * 获取文件文本内容
     * @param path 文件路径
     * @return 文本内容
     */
    @GetMapping("/generator/file")
    public Result<?> getFileContent(String path) {
        return Result.ok(generatorService.getFileContent(path));
    }
}
