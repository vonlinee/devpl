package io.devpl.generator.service;

import io.devpl.generator.compiler.CompilationResult;
import io.devpl.generator.domain.param.CompilerParam;

public interface CompilationService {

    CompilationResult compile(CompilerParam param);

    String getFullClassName(String sourceCode);
}
