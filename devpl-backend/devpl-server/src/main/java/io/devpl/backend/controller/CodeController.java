package io.devpl.backend.controller;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码控制器
 */
@RestController
@RequestMapping("/api/code")
@AllArgsConstructor
public class CodeController {

    /**
     * 编译Java代码
     *
     * @param code Java代码
     */
    @GetMapping(value = "/java/compile")
    public void compile(String code) {

    }

    public void testGroovy() {
        System.out.println("我是SpringBoot框架的成员类，但该方法由Groovy脚本调用");
    }

    @PostMapping("/groovy")
    public String test() {
        //创建GroovyShell
        GroovyShell groovyShell = new GroovyShell();
        //装载解析脚本代码
        Script script = groovyShell.parse("""
                package groovy

                import io.devpl.backend.controller.CodeController
                import io.devpl.backend.utils.SpringUtils

                /**
                 * 静态变量
                 */
                class Globals {
                    static String PARAM1 = "静态变量"
                    static int[] arrayList = [1, 2]
                }

                def getBean() {
                    CodeController controller = SpringUtils.getBean(CodeController.class);
                    controller.testGroovy()
                }
                """);
        //执行
        script.invokeMethod("getBean", null);
        return "ok";
    }
}
