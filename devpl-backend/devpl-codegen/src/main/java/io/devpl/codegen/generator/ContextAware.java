package io.devpl.codegen.generator;

public interface ContextAware {

    /**
     * 注入上下文对象
     *
     * @param context 上下文对象
     */
    default void setContext(Context context) {
    }
}
