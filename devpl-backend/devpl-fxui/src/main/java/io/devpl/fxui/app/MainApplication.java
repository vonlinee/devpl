package io.devpl.fxui.app;

import io.devpl.fxui.components.CodeRegion;
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

        NavigationMenu menu = new NavigationMenu("A", btn);

        menu.addChild("A1", RouterView.of(View.load(IndexView.class)));
        menu.addChild("A2", RouterView.of(View.load(TestView.class)));
        menu.addChild("A3", RouterView.of(new CodeRegion()));

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
