package io.devpl.fxui.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class ColumnValueGeneratorTable extends TreeTableView<Row> {

    public ColumnValueGeneratorTable() {
        TreeTableColumn<Row, String> col1 = new TreeTableColumn<>("Attribute");
        col1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getFieldName()));
        TreeTableColumn<Row, String> col2 = new TreeTableColumn<>("Generator");
        col2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getGeneratorName()));

        getColumns().add(col1);
        getColumns().add(col2);
    }
}
