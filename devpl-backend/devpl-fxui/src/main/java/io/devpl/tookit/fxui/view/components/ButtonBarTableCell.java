package io.devpl.tookit.fxui.view.components;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

/**
 * 单元格包含一个ButtonBar
 *
 * @param <S>
 * @param <T>
 */
public class ButtonBarTableCell<S, T> extends TableCell<S, T> {

    private final HBox container = new HBox();

    public final void addButton(Button button) {
        container.getChildren().add(button);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(container);
        }
    }
}
