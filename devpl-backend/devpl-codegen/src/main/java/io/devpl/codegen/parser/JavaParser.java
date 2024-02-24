package io.devpl.codegen.parser;

import com.github.javaparser.ParserConfiguration;

import java.util.function.Consumer;

/**
 * TODO 封装github java parser库
 */
public class JavaParser {

    private final ParserConfiguration.LanguageLevel languageLevel;
    private final ParserConfiguration configuration;

    public JavaParser(int javaVersion) {
        this.configuration = JavaParserUtils.newSimpleParserConfiguration(javaVersion);
        languageLevel = this.configuration.getLanguageLevel();
    }

    public static JavaParser builder(int javaVersion) {
        return new JavaParser(javaVersion);
    }

    public void config(Consumer<ParserConfiguration> consumer) {
        consumer.accept(this.configuration);
    }

    /**
     * 当前Parser配置是否兼容指定的版本
     *
     * @param javaVersion java版本
     * @return 是否兼容
     */
    public final boolean isSupported(int javaVersion) {
        return languageLevel.isYieldSupported();
    }
}
