package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.compiler.CompilationResult;
import io.devpl.backend.domain.bo.ASTParseResult;
import io.devpl.backend.domain.param.CompilerParam;
import io.devpl.backend.service.CompilationService;
import io.devpl.backend.service.JavaASTService;
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
