package io.devpl.sdk;

/**
 * 可以作为业务类规范和数据库表规范的桥梁
 * @param <T>
 */
public interface NamingStrategy<T> {
    String nameOf(T input);
}
