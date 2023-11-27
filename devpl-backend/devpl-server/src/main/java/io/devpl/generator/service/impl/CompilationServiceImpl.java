package io.devpl.generator.service.impl;

import io.devpl.generator.compiler.CompilationResult;
import io.devpl.generator.compiler.DynamicJavaCompiler;
import io.devpl.generator.domain.param.CompilerParam;
import io.devpl.generator.service.CompilationService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final DynamicJavaCompiler compiler = new DynamicJavaCompiler();

    @Override
    public CompilationResult compile(CompilerParam param) {
        return compiler.compile(getFullClassName(param.getCode()), param.getCode());
    }

    /**
     * 获取类的全名称
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
    @Override
    public String getFullClassName(String sourceCode) {
        String className = "";
        Pattern pattern = Pattern.compile("package\\s+\\S+\\s*;");
        Matcher matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }
        pattern = Pattern.compile("class\\s+\\S+\\s+\\{");
        matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
        }
        return className;
    }
}
