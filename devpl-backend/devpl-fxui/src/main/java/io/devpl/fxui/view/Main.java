package io.devpl.fxui.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        File file = new File(new File("").getAbsolutePath(), "devpl-fxui/src/main/resources/fxml/index.fxml");

        Parent root = FXMLLoader.load(file.toPath().toUri().toURL());

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
