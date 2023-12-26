package io.devpl.fxui.view.field;

import io.devpl.fxui.model.FieldNode;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class FieldParseToolView extends BorderPane {

    FieldTreeTable treeTable;
    TabPane tabPane;

    public FieldParseToolView() {
        SplitPane root = new SplitPane();
        tabPane = new TabPane();
        treeTable = new FieldTreeTable();

        addTab(new SqlParseView());
        addTab(new MyBatisParseView());

        root.getItems().addAll(tabPane, treeTable);

        Button btnGetSample = new Button("获取示例");

        Button btnParse = new Button("解析");


        HBox bottom = new HBox();
        bottom.getChildren().add(btnParse);

        btnGetSample.setOnAction(event -> {
            FieldParseView content = (FieldParseView) tabPane.getSelectionModel().getSelectedItem().getContent();
            content.fillSampleText();
        });

        btnParse.setOnAction(event -> {
            FieldParseView parseView = (FieldParseView) tabPane.getSelectionModel().getSelectedItem().getContent();

            List<FieldNode> fieldNodes = parseView.parse(parseView.getParseableText());

            treeTable.addFields(fieldNodes);
        });

        bottom.getChildren().add(btnGetSample);
        setCenter(root);
        setBottom(bottom);
    }

    void addTab(FieldParseView view) {
        Tab tab = new Tab(view.getName(), view);
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
    }

    public final List<FieldNode> getFields() {
        return treeTable.getFields();
    }
}
