package io.devpl.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
