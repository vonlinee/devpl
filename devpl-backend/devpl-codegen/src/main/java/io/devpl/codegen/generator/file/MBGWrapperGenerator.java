package io.devpl.codegen.generator.file;

import io.devpl.codegen.generator.GenerationTarget;

import java.util.List;

/**
 * 包装 mybatis-generator 的 AbstractGenerator
 * 使用 mybatis 对 java 语法 dom 的定义来生成，通过代码来生成java源文件
 * 其他类似的还有 javapoet(<a href="https://github.com/square/javapoet">javapoet</a>) 这个库
 *
 * @see org.mybatis.generator.codegen.AbstractGenerator
 * @see org.mybatis.generator.api.dom.java.JavaElement
 * @see org.mybatis.generator.api.dom.java.Method
 */
public class MBGWrapperGenerator implements FileGenerator {

    @Override
    public void initialize(GenerationTarget target) {

    }

    @Override
    public List<GeneratedFile> getGeneratedFiles() {
        return null;
    }
}
