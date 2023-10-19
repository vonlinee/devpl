package io.devpl.fxui.common;

import javafx.scene.Node;

public interface NodeBuilder<T extends Node> {

    T build();
}
