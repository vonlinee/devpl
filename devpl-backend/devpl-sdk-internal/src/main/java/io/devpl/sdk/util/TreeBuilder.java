package io.devpl.sdk.util;

import java.util.*;
import java.util.function.Function;

public interface TreeBuilder<K, E, T> {

    Class<K> keyType();

    default List<Function<E, K>> getKeyMappers() {
        return null;
    }

    T apply(Collection<E> collection);

    K getKey(E element, int level);

    T map(int level, K key, E element);

    void next(int level, K parentLevelKey, E element, T parentNode);

    default boolean isKeyEquals(K k1, K k2) {
        return Objects.equals(k1, k2);
    }

    void addChild(T parent, T child, int level, K key);

    Comparator<K> getKeyComparator();

    abstract class ImplBase<_K, _E, _T> implements TreeBuilder<_K, _E, _T> {

        Class<_K> keyType;
        Map<_K, _T> levelParentNodeMap = new HashMap<>();
        List<Function<_E, _K>> keyMappers;

        @Override
        public Class<_K> keyType() {
            return keyType;
        }

        public void addKeyMapper(Function<_E, _K> keyMapper) {
            if (keyMappers == null) {
                keyMappers = new ArrayList<>();
            }
            keyMappers.add(keyMapper);
        }

        @Override
        public _K getKey(_E element, int level) {
            return keyMappers.get(level).apply(element);
        }

        @Override
        public Comparator<_K> getKeyComparator() {
            return null;
        }

        @Override
        public void next(int level, _K parentLevelKey, _E element, _T parentNode) {
            if (level == keyMappers.size()) {
                return;
            }
            _K levelKey = getKey(element, level);
            if (levelParentNodeMap.containsKey(levelKey)) {
                parentNode = levelParentNodeMap.get(levelKey);
            } else {
                _T node = map(level, levelKey, element);
                if (parentNode != null) {
                    addChild(parentNode, node, level, levelKey);
                    levelParentNodeMap.put(levelKey, node);
                }
                parentNode = node;
            }
            next(level + 1, parentLevelKey, element, parentNode);
        }

        @Override
        public _T apply(Collection<_E> collection) {
            if (this.keyMappers == null) {
                this.keyMappers = getKeyMappers();
            }
            int level = 0;
            _T root = map(-1, null, null);
            for (_E element : collection) {
                next(level, null, element, root);
            }
            return root;
        }
    }
}
