package io.devpl.fxui.layout;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

/**
 * 内容区域
 */
public class ContentContainer extends ScrollPane {

    public ContentContainer() {
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

        // 绑定内容元素的宽高
        widthProperty().addListener((observable, oldValue, newValue) -> {
            Node content = getContent();
            if (content instanceof Region region) {
                region.setPrefWidth(newValue.doubleValue());
            }
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            Node content = getContent();
            if (content instanceof Region region) {
                region.setPrefHeight(newValue.doubleValue());
            }
        });
    }

    public final void switchTo(Node node) {
        if (node instanceof Region region) {
            region.setPrefWidth(getPrefWidth());
            region.setPrefHeight(getPrefHeight());
        }
        setContent(node);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }
}
