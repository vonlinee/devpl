package io.devpl.fxui.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FXControl {

    public static Button button(String text, EventHandler<ActionEvent> actionEventEventHandler) {
        Button button = new Button(text);
        button.setOnAction(actionEventEventHandler);
        return button;
    }

    public static Label label(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER_LEFT);
        return label;
    }
}
