package io.devpl.fxui.view;

import io.devpl.fxui.utils.FXUtils;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class TemplateManageView extends BorderPane {

    public TemplateManageView() {

        TableView<TemplateInfo> tableView = FXUtils.createTableView(TemplateInfo.class);

        setCenter(tableView);
    }
}
