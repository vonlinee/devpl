package io.devpl.fxui.view;

import io.devpl.fxui.utils.FXUtils;
import javafx.scene.layout.BorderPane;

public class TypeMappingTable extends BorderPane {

    public TypeMappingTable() {
        setCenter(FXUtils.createTableView(TypeMappingItem.class));
    }
}
