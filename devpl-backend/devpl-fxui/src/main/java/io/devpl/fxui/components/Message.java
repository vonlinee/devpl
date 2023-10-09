package io.devpl.fxui.components;

import javafx.scene.control.Alert;

/**
 * 消息弹窗
 */
public class Message {

    private static final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    static {
        alert.setResizable(true);
    }

    public static void show(String message) {
        alert.setContentText(message);
        alert.show();
    }
}
