package io.devpl.generator.controller;

import io.devpl.generator.common.query.Result;
import io.devpl.generator.compiler.CustomStringJavaCompiler;
import io.devpl.generator.domain.param.CompilerParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/compiler")
public class CompilerController {

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
        CustomStringJavaCompiler compiler = new CustomStringJavaCompiler(param.getCode());
        boolean res = compiler.compile();
        StringBuilder result = new StringBuilder();
        if (res) {
            result.append("编译成功");
            result.append("compilerTakeTime：").append(compiler.getCompilerTakeTime());
            try {
                compiler.runMainMethod();
                result.append("runTakeTime：").append(compiler.getRunTakeTime());
            } catch (Exception e) {
                result.append(e.getMessage());
            }
            result.append(compiler.getRunResult())
                .append("诊断信息：")
                .append(compiler.getCompilerMessage());
        } else {
            result.append("编译失败")
                .append(compiler.getCompilerMessage());
        }
        return Result.ok(result.toString());
    }
}
