package io.devpl.fxui.controls;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 弹窗
 *
 * @see javafx.stage.PopupWindow
 */
public class Modal extends Stage {

    public static void show(Event event, Parent root, EventHandler<WindowEvent> onClose) {
        if (root.getParent() != null) {
            throw new RuntimeException("root node has been add scene graph");
        }
        Scene scene = root.getScene();
        if (scene == null) {
            scene = new Scene(root);
        }
        Modal modal = new Modal();
        modal.setScene(scene);
        modal.setOnCloseRequest(onClose);
        modal.show();
    }
}
