package io.devpl.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
@AllArgsConstructor
public class CodeController {

    @GetMapping(value = "/java/compile")
    public void compile(String code) {

    }
}
