package io.devpl.codegen.type;

import java.util.Collection;
import java.util.function.Consumer;

public interface TypeInferenceStrategy<T> extends Consumer<Collection<T>> {

    int LOWEST_PRIORITY = Integer.MIN_VALUE;

    default void addStrategy(TypeInferenceStrategy<T> strategy) {
    }

    /**
     * @param collection 只读，不会增删元素，只会修改元素
     */
    void inferType(Collection<T> collection);

    @Override
    default void accept(Collection<T> collection) {
        inferType(collection);
    }

    void setActive(boolean active);

    boolean isActive();

    int getPriority();
}
