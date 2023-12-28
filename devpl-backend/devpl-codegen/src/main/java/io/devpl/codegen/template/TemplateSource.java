package io.devpl.codegen.template;

import java.io.OutputStream;

/**
 * 为了统一不同的模板引擎的实现，将各个模板引擎的类进行包装
 */
public interface TemplateSource {

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
     * 此模板是否真实存在
     *
     * @return 模板是否存在
     */
    default boolean exists() {
        return true;
    }

    /**
     * 渲染
     *
     * @param engine    模板引擎
     * @param arguments 模板参数
     */
    default void render(TemplateEngine engine, TemplateArguments arguments, OutputStream output) {
    }

    /**
     * 模板未找到时返回此值
     */
    TemplateSource UNKNOWN = new TemplateSource() {
        @Override
        public boolean exists() {
            return false;
        }
    };
}
