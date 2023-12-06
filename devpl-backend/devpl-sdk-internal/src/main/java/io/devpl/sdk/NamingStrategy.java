package io.devpl.sdk;

public interface NamingStrategy<T> {
    String name(T input);
}
