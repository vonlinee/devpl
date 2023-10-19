package io.devpl.fxui.common;

import javafx.scene.control.Label;

public class LabelBuilder implements NodeBuilder<Label> {

    private final Label label;

    private LabelBuilder(Label label) {
        this.label = label;
    }

    public static LabelBuilder builder() {
        return new LabelBuilder(new Label());
    }

    private LabelBuilder text(String text) {
        label.setText(text);
        return this;
    }


    @Override
    public Label build() {
        return label;
    }
}
