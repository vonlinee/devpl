package io.devpl.fxui.components;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @see javafx.stage.Popup
 */
public class Modal extends Stage {

    private final ContentRegion contentRegion;

    public Modal() {
        this(400.0, 500.0);
    }

    public Modal(double width, double height) {
        setScene(new Scene(contentRegion = new ContentRegion(), 400.0, 400.0));
    }

    public final void setContent(Node content) {
        this.contentRegion.setContent(content);
    }

    public static void show(String title, Parent node) {
        Modal modal = new Modal();
        modal.setTitle(title);
        modal.setContent(node);
        modal.show();
    }

    public static void show(String title, Region region) {
        Modal modal = new Modal();
        modal.setTitle(title);
        modal.setWidth(region.getWidth());
        modal.setHeight(region.getHeight());
        modal.setContent(region);
        modal.show();
    }
}
