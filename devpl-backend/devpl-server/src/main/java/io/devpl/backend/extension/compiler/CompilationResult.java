package io.devpl.backend.extension.compiler;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 储存编译结果
 */
@Setter
@Getter
public class CompilationResult {

    /**
     * 类限定名称
     */
    private String fullClassName;

    /**
     * 源码
     */
    private String sourceCode;

    /**
     * 编译耗时(单位ms)
     */
    private long compilerTakeTime;

    /**
     * 运行耗时(单位ms)
     */
    private long runTakeTime;

    /**
     * true:编译成功 false:编译失败
     */
    private boolean failed;

    public StringBuilder compileMessage = new StringBuilder();

    private Map<String, Class<?>> compiledClassMap = new HashMap<>();

    public void addCompiledClass(String fullClassName, Class<?> clazz) {
        compiledClassMap.put(fullClassName, clazz);
    }

    public void addCompiledClass(Class<?> clazz) {
        compiledClassMap.put(clazz.getName(), clazz);
    }

    public Class<?> getClass(String name) {
        return compiledClassMap.get(name);
    }

    public void start() {
        this.compilerTakeTime = System.currentTimeMillis();
    }

    public void stop() {
        this.compilerTakeTime = System.currentTimeMillis() - compilerTakeTime;
    }

    @Override
    public String toString() {
        return "CompilationResult{" +
            "fullClassName='" + fullClassName + '\'' +
            ", sourceCode='" + sourceCode + '\'' +
            ", compilerTakeTime=" + compilerTakeTime +
            ", runTakeTime=" + runTakeTime +
            ", failed=" + failed +
            ", compileMessage='" + compileMessage + '\'' +
            '}';
    }

    public String prettyPrint() {
        return this.toString();
    }

    public CompilationResult appendMsg(Object message) {
        compileMessage.append(message);
        return this;
    }
}
