package io.devpl.fxui.app;

import io.fxtras.control.SplitPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TestApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        SplitPane root = new SplitPane();

        AnchorPane pane1 = new AnchorPane();
        pane1.setStyle("-fx-background-color: red");
        AnchorPane pane2 = new AnchorPane();
        pane2.setStyle("-fx-background-color: yellow");
        AnchorPane pane3 = new AnchorPane();
        pane3.setStyle("-fx-background-color: green");

        root.getItems().addAll(pane1, pane2, pane3);

        stage.setTitle("工具");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
