package io.devpl.fxui.app;

import io.devpl.fxui.components.Modal;
import io.devpl.fxui.mvvm.View;
import io.devpl.fxui.view.FileHelperView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class ToolsApplication extends Application {

    List<Class<?>> list = new ArrayList<>();

    @Override
    public void init() throws Exception {
        super.init();

        list.add(FileHelperView.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();

        ListView<Class<?>> listView = new ListView<>();

        listView.setCellFactory(TextFieldListCell.forListView(new StringConverter<>() {
            @Override
            public String toString(Class<?> object) {
                return object.getName();
            }

            @Override
            public Class<?> fromString(String string) {
                try {
                    return Class.forName(string);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        listView.getItems().addAll(this.list);

        ToolBar toolBar = new ToolBar();

        Button button = new Button("Open");

        button.setOnAction(event -> {
            Class<?> selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Modal.show("", View.load(selectedItem));
            }
        });

        toolBar.getItems().add(button);
        root.setTop(toolBar);
        root.setCenter(listView);
        Scene scene = new Scene(root, 800, 640);
        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
    }
}
