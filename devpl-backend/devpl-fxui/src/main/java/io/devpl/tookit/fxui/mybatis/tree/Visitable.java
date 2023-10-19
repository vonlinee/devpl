package io.devpl.tookit.fxui.mybatis.tree;

public interface Visitable<T> {
 
    void accept(Visitor<T> visitor);
}
