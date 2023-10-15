package io.devpl.fxui.app;

import io.devpl.fxui.components.CodeRegion;
import io.devpl.fxui.components.StepPane;
import io.devpl.fxui.layout.LayoutPane;
import io.devpl.fxui.layout.menu.NavigationMenu;
import io.devpl.fxui.layout.menu.RouterView;
import io.devpl.fxui.mvvm.View;
import io.devpl.fxui.view.IndexView;
import io.devpl.fxui.view.TestView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutPane layoutPane = new LayoutPane();

        Button btn = new Button("AAA");

        NavigationMenu menu = new NavigationMenu("开发工具", btn);

//        menu.addChild("代码生成", RouterView.of(View.load(IndexView.class)));
//        menu.addChild("模板管理", RouterView.of(View.load(TestView.class)));
//        menu.addChild("模拟数据", RouterView.of(new CodeRegion()));
        menu.addChild("测试控件", RouterView.of(new StepPane()));

        layoutPane.addNavigationMenu(menu);

        layoutPane.expandAllMenu();

        Scene scene = new Scene(layoutPane, 800, 640);
        stage.setTitle("Devpl");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
