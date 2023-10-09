package io.devpl.fxui.layout.menu;

import javafx.scene.Node;

/**
 * TODO 懒加载
 */
public class RouterView {

    private Node node;

    public RouterView(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public static RouterView of(Node node) {
        return new RouterView(node);
    }
}
