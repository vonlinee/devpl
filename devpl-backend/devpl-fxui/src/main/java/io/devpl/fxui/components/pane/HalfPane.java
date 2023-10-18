package io.devpl.fxui.components.pane;

import io.devpl.fxui.utils.FXStyle;
import io.devpl.fxui.utils.FXUtils;
import javafx.scene.control.SplitPane;
import javafx.scene.control.skin.SplitPaneSkin;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @see javafx.scene.control.SplitPane
 * @see SplitPaneSkin#SplitPaneSkin(SplitPane)
 */
public class HalfPane extends Region {

    Rectangle rectangle = new Rectangle();

    public HalfPane() {
        getChildren().add(rectangle);

        rectangle.setWidth(400.0);
        rectangle.setHeight(400.0);

        rectangle.setStroke(Color.RED);

        this.setStyle(FXStyle.of().backgroundColor(Color.RED).build());
    }

    @Override
    protected void layoutChildren() {
        FXUtils.layoutInRegion(this, rectangle);
    }
}
