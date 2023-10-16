package io.devpl.fxui.components;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @see javafx.stage.Popup
 */
public class Modal extends Stage {

    private final ContentRegion contentRegion;

    public Modal() {
        setScene(new Scene(contentRegion = new ContentRegion(), 400.0, 400.0));
    }

    public void setContent(Node content) {
        this.contentRegion.setContent(content);
    }

    public static void show(String title, Node node) {
        Modal modal = new Modal();
        modal.setTitle(title);
        modal.setAlwaysOnTop(true);
        modal.setContent(node);
        modal.show();
    }
}
