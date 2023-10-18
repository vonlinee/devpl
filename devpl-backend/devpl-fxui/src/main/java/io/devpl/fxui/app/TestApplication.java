package io.devpl.fxui.app;

import io.devpl.fxui.components.Loading;
import io.devpl.fxui.components.pane.HalfPane;
import io.devpl.fxui.utils.FXControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        HalfPane halfPane = new HalfPane();
        halfPane.setPrefSize(200, 200);

        VBox box = new VBox();

        Scene scene = new Scene(box, 800, 640);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(200, 200);
        anchorPane.setStyle("-fx-background-color: #a7ab88");

        Loading loading = Loading.wrap(anchorPane);

        box.getChildren().add(FXControl.button("show", event -> loading.show("hello")));
        box.getChildren().add(FXControl.button("hide", event -> loading.hide()));

        box.getChildren().add(loading);

        box.getChildren().add(FXControl.button("hide", event -> loading.setMaxOpacity(loading.getMaxOpacity())));

        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
    }
}
