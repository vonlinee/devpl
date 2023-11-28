package io.devpl.backend.compiler;

import lombok.Getter;
import lombok.Setter;

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

    private String compileMessage;

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
}
