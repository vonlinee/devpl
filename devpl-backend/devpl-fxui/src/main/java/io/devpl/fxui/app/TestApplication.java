package io.devpl.fxui.app;

import io.devpl.fxui.components.Modal;
import io.devpl.fxui.components.pane.HalfPane;
import io.devpl.fxui.mvvm.View;
import io.devpl.fxui.view.FileHelperView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class TestApplication extends Application {


    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        HalfPane halfPane = new HalfPane();

        Scene scene = new Scene(halfPane, 800, 640);
        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
    }
}
