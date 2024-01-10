package io.fxtras.utils;

import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;

public class FXUtils {

    public static void layoutRoot(Region region, Node root) {
        Region.layoutInArea(root, 0, 0, region.getWidth(), region.getHeight(), 0, new Insets(0), true, true, HPos.CENTER, VPos.CENTER, false);
    }

    public static Background newBackground(Paint color) {
        return new Background(new BackgroundFill(color, new CornerRadii(0), new Insets(0)));
    }

    public static Border newBorder(Paint color) {
        return new Border(new BorderStroke(color, BorderStrokeStyle.NONE, new CornerRadii(0), new BorderWidths(0)));
    }

    /**
     * 兼容NPE
     *
     * @param bool 布尔值
     * @return 是否为true
     */
    public static boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    /**
     * 兼容NPE
     *
     * @param bool 布尔值
     * @return 是否为false
     */
    public static boolean isFalse(Boolean bool) {
        return bool != null && !bool;
    }

    /**
     * 获取Stage
     *
     * @param event 事件对象
     * @return Stage
     */
    @Nullable
    public static Stage getStage(Event event) {
        Object eventSource = event.getSource();
        if (eventSource instanceof Node node) {
            Scene scene = node.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window instanceof Stage stage) {
                    return stage;
                }
            }
        }
        return null;
    }
}
