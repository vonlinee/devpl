package io.devpl.codegen.generator.config;

/**
 * 作为配置信息的对象
 *
 * @see io.devpl.codegen.generator.PropertyRegistry
 */
public interface PropertyObject {

    void addProperty(String name, String value);

    String getProperty(String name, String defaultValue);

    boolean containsKey(String name);
}
