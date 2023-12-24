package io.devpl.fxui;

import io.devpl.fxui.view.FieldParseToolView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 程序入口
 */
public class MainApplication extends Application {
    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(new FieldParseToolView(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
