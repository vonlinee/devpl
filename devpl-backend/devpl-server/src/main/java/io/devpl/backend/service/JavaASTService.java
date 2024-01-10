package io.devpl.backend.service;

import io.devpl.common.model.ASTParseResult;

/**
 * Java源代码解析
 */
public interface JavaASTService {

    ASTParseResult parse(String source);
}
