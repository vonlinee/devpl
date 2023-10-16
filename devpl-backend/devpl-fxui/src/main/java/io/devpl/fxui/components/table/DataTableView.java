package io.devpl.fxui.components.table;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

/**
 * 扩展TableView
 * @param <S>
 * @see TableView
 */
public class DataTableView<S> extends TableView<S> {

    public DataTableView() {
        setEditable(false);
        setRowFactory(param -> {
            TableRow<S> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

            });
            return row;
        });
    }
}
