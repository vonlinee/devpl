package io.devpl.backend.service;

import io.devpl.backend.compiler.CompilationResult;
import io.devpl.backend.domain.param.CompilerParam;

public interface CompilationService {

    CompilationResult compile(CompilerParam param);

    String getFullClassName(String sourceCode);
}
