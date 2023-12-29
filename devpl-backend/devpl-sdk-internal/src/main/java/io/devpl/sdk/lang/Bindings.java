package io.devpl.sdk.lang;

public interface Bindings {
    Object get(String name);

    class Array implements Bindings {
        private final Object[] array;

        public Array(Object[] array) {
            this.array = array;
        }

        @Override
        public Object get(String name) {
            return array[Integer.parseInt(name)];
        }
    }
}
