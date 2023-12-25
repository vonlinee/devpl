package io.devpl.fxui.tools.mybatis;

public interface Visitable<T> {

    void accept(Visitor<T> visitor);
}
