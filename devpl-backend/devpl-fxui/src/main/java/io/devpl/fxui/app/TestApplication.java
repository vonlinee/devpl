package io.devpl.fxui.app;

import io.github.palexdev.materialfx.controls.MFXTextField;
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


        AnchorPane root = new AnchorPane();

        root.getChildren().add(new MFXTextField());

        stage.setTitle("工具");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
