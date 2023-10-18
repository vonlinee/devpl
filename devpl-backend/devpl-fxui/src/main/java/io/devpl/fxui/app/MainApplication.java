package io.devpl.fxui.app;

import io.devpl.fxui.components.Modal;
import io.devpl.fxui.components.RouterPane;
import io.devpl.fxui.components.RouterView;
import io.devpl.fxui.layout.LayoutPane;
import io.devpl.fxui.layout.menu.NavigationMenu;
import io.devpl.fxui.view.MockGeneratorView;
import io.devpl.fxui.view.TemplateManageView;
import io.devpl.fxui.view.TypeMappingTable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();

        LayoutPane layoutPane = new LayoutPane();

        TextField textField = new TextField();
        Button btn = new Button("AAA");

        NavigationMenu menu = new NavigationMenu("开发工具", null);

        RouterPane routerPane = new RouterPane();

        menu.addChild("模拟数据", RouterView.of(new MockGeneratorView()));
        menu.addChild("测试控件", RouterView.of(routerPane));
        menu.addChild("模板管理", RouterView.of(new TemplateManageView()));

        layoutPane.addNavigationMenu(menu);

        layoutPane.expandAllMenu();

        HBox hBox = new HBox();

        hBox.getChildren().addAll(textField, btn);

        routerPane.addRouteMapping(1, new Button("111"));
        routerPane.addRouteMapping(2, new Button("222"));
        routerPane.addRouteMapping(3, new Button("333"));

        btn.setOnAction(event -> {
//            String text = textField.getText();
//            if (text != null && !text.isBlank()) {
//                int key = Integer.parseInt(text);
//                routerPane.setCurrentRoute(key);
//            }

            Modal.show("hello", new TypeMappingTable(), 600, 500);
        });

        root.setTop(hBox);

        root.setCenter(layoutPane);

        Scene scene = new Scene(root, 800, 640);
        stage.setTitle("Devpl");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
