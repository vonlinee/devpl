package io.devpl.backend.service;

import io.devpl.backend.domain.bo.ASTParseResult;

/**
 * Java源代码解析
 */
public interface JavaASTService {

    ASTParseResult parse(String source);
}
