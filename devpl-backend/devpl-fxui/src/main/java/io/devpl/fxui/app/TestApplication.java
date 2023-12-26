package io.devpl.fxui.app;

import io.devpl.fxui.controller.template.TemplateManageView;
import io.fxtras.mvvm.View;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestApplication extends Application {

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {


        Parent root = View.load(TemplateManageView.class);
//        TypeMappingTable table = new TypeMappingTable();

        stage.setTitle("工具");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
