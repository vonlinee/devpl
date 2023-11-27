package io.devpl.generator.controller;

import io.devpl.generator.common.query.Result;
import io.devpl.generator.compiler.CompilationResult;
import io.devpl.generator.domain.bo.ASTParseResult;
import io.devpl.generator.domain.param.CompilerParam;
import io.devpl.generator.service.CompilationService;
import io.devpl.generator.service.JavaASTService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/compiler")
public class CompilerController {

    @Resource
    CompilationService compilationService;
    @Resource
    JavaASTService javaASTService;

    /**
     * 获取示例文本
     *
     * @return 示例文本
     */
    @GetMapping("/smaple")
    public Result<String> getSampleCode() {
        return Result.ok("""
            public class HelloWorld {
                public static void main(String []args) {
                    for(int i=0; i < 1; i++){
                        System.out.println("Hello World!");
                    }
                }
            }
            """);
    }

    @PostMapping(value = "/compile")
    public Result<String> compile(@RequestBody CompilerParam param) {
        CompilationResult result = compilationService.compile(param);
        return Result.ok(result.prettyPrint());
    }

    @PostMapping("/parse")
    public Result<ASTParseResult> parse(@Validated @RequestBody CompilerParam param) {
        return Result.ok(javaASTService.parse(param.getCode()));
    }
}
