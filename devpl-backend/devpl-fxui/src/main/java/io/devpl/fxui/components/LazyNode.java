package io.devpl.fxui.components;

import javafx.scene.Node;

public abstract class LazyNode {

    Node node;

    public Node getNode() {
        if (node == null) {
            node = render();
        }
        return node;
    }

    protected abstract Node render();
}
