package io.devpl.fxui.app;

import io.devpl.fxui.view.TypeMappingTable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        TypeMappingTable table = new TypeMappingTable();

        stage.setTitle("工具");
        stage.setScene(new Scene(table, 800, 500));
        stage.show();
    }
}
