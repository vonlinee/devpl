package io.devpl.codegen.meta;

import com.github.javaparser.ast.CompilationUnit;

public interface CompilationUnitVisitor<T> {

    T visit(CompilationUnit cu);
}
