package io.devpl.codegen.strategy;

import io.devpl.codegen.core.CaseFormat;

/**
 * 命名转换策略
 *
 * @see CaseFormat
 */
public interface NamingChangeStrategy {

    /**
     * 命名策略实现类的唯一ID
     *
     * @return 命名策略实现类的唯一ID
     */
    default String getId() {
        return toString();
    }

    /**
     * 按指定命名策略进行转换
     *
     * @param source 原字符串
     * @return 转换结果
     */
    String apply(String source);

    /**
     * 不做改变
     */
    NamingChangeStrategy NO_CHANGE = new NamingChangeStrategy() {
        @Override
        public String getId() {
            return "NO_CHANGE";
        }

        @Override
        public String apply(String source) {
            return source == null ? "" : source;
        }
    };
}