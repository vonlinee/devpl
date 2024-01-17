package io.devpl.backend.common;

/**
 * 枚举值
 *
 * @see org.apache.ibatis.type.BaseTypeHandler
 */
public interface ValueEnum<K, V> {

    K getKey();

    V getValue();
}
