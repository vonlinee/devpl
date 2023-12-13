package io.devpl.sdk;

public interface Visitable<T> {

    void accept(Visitor<T> visitor);
}
