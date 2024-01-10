package io.devpl.backend.service.impl;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.printer.configuration.DefaultConfigurationOption;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import io.devpl.common.model.ASTParseResult;
import io.devpl.backend.service.JavaASTService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * javaparser
 * Java Parser是基于JavaCC做为Java语言词法解析的工具，支持Java语言生成AST，在AST基础上进行类型推断分析，支持修改AST从而生成新的Java文件内容，支持从Java
 * 1.0到14所有的版本的AST解析
 * <a href="https://github.com/javaparser/javaparser">...</a>
 */
@Service
public class GithubJavaParserASTService implements JavaASTService {

    private final JavaParser parser;
    // 打印配置
    private final PrinterConfiguration printerConfiguration;

    public GithubJavaParserASTService() {
        ParserConfiguration parserConfig = new ParserConfiguration();
        parserConfig.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8); // JDK8
        parserConfig.setCharacterEncoding(StandardCharsets.UTF_8); // 源代码字符编码
        // 关闭注释分析 默认情况下启用注释分析，禁用将加快解析速度，但在处理单个源文件时速度提升不明显，如果要解析大量文件，建议禁用。
        parserConfig.setAttributeComments(true);
        // 设置为孤立注释
        parserConfig.setDoNotAssignCommentsPrecedingEmptyLines(true);
        parser = new JavaParser(parserConfig);

        printerConfiguration = new DefaultPrinterConfiguration();
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_COMMENTS, true));
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.PRINT_JAVADOC, true));
        printerConfiguration.addOption(new DefaultConfigurationOption(DefaultPrinterConfiguration.ConfigOption.COLUMN_ALIGN_FIRST_METHOD_CHAIN, true));
    }

    @Override
    public ASTParseResult parse(String source) {

        ParseResult<CompilationUnit> parse = parser.parse(source);

        if (parse.isSuccessful()) {
            Optional<CompilationUnit> result = parse.getResult();
            if (result.isPresent()) {
                CompilationUnit compilationUnit = result.get();

                List<Comment> comments = compilationUnit.getAllComments();

                NodeList<ImportDeclaration> imports = compilationUnit.getImports();

                NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();

                System.out.println(types);
            }
        }

        return new ASTParseResult();
    }
}
