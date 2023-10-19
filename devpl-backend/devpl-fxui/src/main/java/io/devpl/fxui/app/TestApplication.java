package io.devpl.fxui.app;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.components.Loading;
import io.devpl.fxui.components.pane.HalfPane;
import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.view.DataTypeModel;
import io.devpl.fxui.view.TypeMappingTable;
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

        TypeMappingTable table = new TypeMappingTable();

        stage.setTitle("工具");
        stage.setScene(new Scene(table, 800, 500));
        stage.show();
    }
}
