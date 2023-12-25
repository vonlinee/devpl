package io.devpl.fxui.components;

import javafx.scene.Node;

/**
 * TODO 懒加载
 */
public class RouterView {

    private final Node node;

    public RouterView(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public static RouterView of(Node node) {
        return new RouterView(node);
    }

    public boolean isEmpty() {
        return node == null;
    }
}
