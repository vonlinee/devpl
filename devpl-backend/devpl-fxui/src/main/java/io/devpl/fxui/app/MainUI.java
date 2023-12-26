package io.devpl.fxui.app;

import io.devpl.fxui.controller.MainView;
import io.fxtras.JavaFXApplication;
import io.fxtras.mvvm.View;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends JavaFXApplication {

    @Override
    protected void onInit() throws Exception {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(View.load(MainView.class)));
        primaryStage.show();
    }
}
