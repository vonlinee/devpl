package io.devpl.generator.service;

import io.devpl.generator.domain.bo.ASTParseResult;

/**
 * Java源代码解析
 */
public interface JavaASTService {

    ASTParseResult parse(String source);
}
