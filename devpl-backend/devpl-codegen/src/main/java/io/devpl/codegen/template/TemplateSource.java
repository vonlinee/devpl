package io.devpl.codegen.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 为了统一不同的模板引擎的实现，将各个模板引擎的类进行包装
 */
public interface TemplateSource {

    /**
     * 模板名称
     *
     * @return 模板名称，通过此名称来检索已存在的模板
     */
    @NotNull String getName();

    /**
     * 设置模板名称
     */
    default void setName(String templateName) {
    }

    /**
     * @return 获取实际的模板
     * @see StringTemplateSource
     * @see FileTemplateSource
     */
    @NotNull
    default TemplateSource getSource() {
        return this;
    }

    /**
     * @return 可能是字符串模板，或文件路径，或者文件地址URL等
     */
    default String getContent() {
        return "";
    }

    /**
     * 设置模板的内容
     *
     * @param content 模板内容
     */
    default void setContent(String content) {
    }

    /**
     * 是否是字符串模板
     *
     * @return 如果是字符串模板，则获取模板内容时调用getContent方法
     * @see TemplateSource#getContent()
     */
    default boolean isStringTemplate() {
        return false;
    }

    /**
     * 此模板是否真实存在
     *
     * @return 模板是否存在
     */
    default boolean exists() {
        return true;
    }

    /**
     * 模板未找到时返回此值
     */
    TemplateSource UNKNOWN = new TemplateSource() {
        @Override
        public @NotNull String getName() {
            return "Template Not Found";
        }

        @Override
        public boolean exists() {
            return false;
        }
    };
}
