package io.devpl.codegen.util;

public interface ContextObject {

    /**
     * 初始化操作
     */
    default void initialize() {
    }

    /**
     * 获取单例对象
     *
     * @param type 类型
     * @param <T>  对象类型
     * @return 数据对象
     */
    <T> T getObject(Class<T> type);

    /**
     * 获取单例对象
     *
     * @param type 类型
     * @param <T>  对象类型
     * @return 数据对象
     */
    <T> T getObject(Class<T> type, T defaultValue);

    /**
     * 存放数据
     *
     * @param object 对象，key为对象的Class类型，重复则被覆盖
     * @param <T>    对象类型
     * @return 对象数据
     */
    <T> T putObject(T object);

    /**
     * 存放数据
     *
     * @param type   key类型
     * @param object 对象，key为对象的Class类型，重复则被覆盖
     * @param <T>    对象类型
     * @return 对象数据
     */
    <C extends T, T> T putObject(Class<T> type, C object);

    /**
     * 指定key存放数据
     *
     * @param key    指定key
     * @param object 对象，key为对象的Class类型，重复则被覆盖
     * @param <T>    对象类型
     * @return 对象数据
     */
    <T> T put(String key, T object);

    /**
     * 获取数据
     *
     * @param key          指定key
     * @param defaultValue 默认值
     * @param <T>          指定key的数据
     * @return 存放数据
     */
    <T> T get(String key, T defaultValue);

    /**
     * 是否包含指定key的数据
     *
     * @param key key类型，Class或者字符串，如果不是Class或者字符串，则获取该对象的Class
     * @return 是否包含指定key的数据
     */
    boolean contains(Object key);
}
