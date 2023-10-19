package io.devpl.fxui.app;

import io.devpl.fxui.controller.MainView;
import io.devpl.fxui.model.ConnectionRegistry;
import io.fxtras.JavaFXApplication;
import io.devpl.fxui.mvvm.View;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends JavaFXApplication {

    @Override
    protected void onInit() throws Exception {
        try {
            ConnectionRegistry.getRegisteredConnectionConfigMap();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Devpl");
        primaryStage.setScene(new Scene(View.load(MainView.class)));
        primaryStage.show();
    }
}
