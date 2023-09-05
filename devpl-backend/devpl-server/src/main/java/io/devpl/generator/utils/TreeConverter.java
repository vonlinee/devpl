package io.devpl.generator.utils;

import java.util.List;
import java.util.function.Function;

/**
 * 树形结构数据转换器
 * <p>
 * 每层父节点需要自定义父节点名称
 * @param <E> 列表
 * @param <T> 树形结构的数据，一般来说包含子节点集合
 */
public class TreeConverter<E, T> {

    /**
     * 节点ID
     */
    Function<E, Object> idSupplier;
    private List<E> list;
    private int currentLevel;

    public T buildFirst(List<E> list) {
        return null;
    }

    public T convert() {
        E firstElement = list.get(0);
        for (E element : list) {

        }
        return null;
    }
}
