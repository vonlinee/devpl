package io.devpl.sdk;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A pool of {@link Constant}s.
 *
 * @param <T> the type of the constant
 */
public abstract class ConstantPool<T extends Constant<T>> {

    /**
     * 保存所有常量实例
     */
    private final ConcurrentMap<String, T> constants = new ConcurrentHashMap<>();

    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Shortcut of {@link #valueOf(String) valueOf(firstNameComponent.getName() +
     * "#" + secondNameComponent)}.
     */
    public T valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return valueOf(checkNotNull(firstNameComponent, "firstNameComponent").getName() + '#' + checkNotNull(secondNameComponent, "secondNameComponent"));
    }

    protected Class<?> checkNotNull(Class<?> firstNameComponent, String string) {
        return Object.class;
    }

    protected Class<?> checkNotNull(String secondNameComponent, String string) {
        return Object.class;
    }

    /**
     * Returns the {@link Constant} which is assigned to the specified {@code name}.
     * If there's no such {@link Constant}, a new one will be created and returned.
     * Once created, the subsequent calls with the same {@code name} will always
     * return the previously created one (i.e. singleton.)
     *
     * @param name the name of the {@link Constant}
     */
    public T valueOf(String name) {
        return getOrCreate(checkNonEmpty(name, "name"));
    }

    /**
     * Get existing constant by name or creates new one if not exists. Threadsafe
     *
     * @param name the name of the {@link Constant}
     */
    private T getOrCreate(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }
        return constant;
    }

    /**
     * Returns {@code true} if a {@link } exists for the given
     * {@code name}.
     */
    public boolean exists(String name) {
        return constants.containsKey(checkNonEmpty(name, "name"));
    }

    /**
     * Creates a new {@link Constant} for the given {@code name} or fail with an
     * {@link IllegalArgumentException} if a {@link Constant} for the given
     * {@code name} exists.
     */
    public T newInstance(String name) {
        return createOrThrow(checkNonEmpty(name, "name"));
    }

    protected String checkNonEmpty(String name, String string) {
        if (name == null || name.isEmpty()) {
            return string;
        }
        return name;
    }

    /**
     * Creates constant by name or throws exception. Threadsafe
     *
     * @param name the name of the {@link Constant}
     */
    private T createOrThrow(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }

        throw new IllegalArgumentException(String.format("'%s' is already in use", name));
    }

    protected abstract T newConstant(int id, String name);

    public final int nextId() {
        return nextId.getAndIncrement();
    }
}
