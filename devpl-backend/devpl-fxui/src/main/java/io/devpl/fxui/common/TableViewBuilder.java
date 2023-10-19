package io.devpl.fxui.common;

import javafx.scene.control.TableView;

public class TableViewBuilder<S> implements NodeBuilder<TableView<S>> {

    Class<S> modelClass;

    private TableView<S> tableView;

    public TableViewBuilder<S> modelClass(Class<S> modelClass) {
        this.modelClass = modelClass;
        return this;
    }

    public TableViewBuilder<S> column(String name) {
        return this;
    }

    @Override
    public TableView<S> build() {
        return tableView;
    }
}
