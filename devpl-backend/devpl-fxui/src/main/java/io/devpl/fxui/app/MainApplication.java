package io.devpl.fxui.app;

import io.devpl.fxui.components.RouterPane;
import io.devpl.fxui.components.RouterView;
import io.devpl.fxui.controller.fields.FieldsManageView;
import io.devpl.fxui.layout.LayoutPane;
import io.devpl.fxui.layout.menu.NavigationMenu;
import io.devpl.fxui.mvvm.View;
import io.devpl.fxui.view.IndexView;
import io.devpl.fxui.view.TypeMappingTable;
import io.fxtras.control.SplitPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        View.load(IndexView.class);

        // menu.addChild("模拟数据", RouterView.of(new MockGeneratorView()));
        // menu.addChild("测试控件", RouterView.of(routerPane));

//        menu.addChild("字段管理", RouterView.of(View.load(FieldsManageView.class)));

        layoutPane.addNavigationMenu(menu);

        layoutPane.expandAllMenu();

        HBox hBox = new HBox();

        hBox.getChildren().addAll(textField, btn);

        routerPane.addRouteMapping(1, new Button("111"));
        routerPane.addRouteMapping(2, new Button("222"));
        routerPane.addRouteMapping(3, new Button("333"));

        menu.addChild("数据类型", RouterView.of(new TypeMappingTable()));

//        Modal modal = Modal.of("数据类型表", typeMappingTable, 700, 500);

        Label label = new Label("hello");
        hBox.getChildren().add(label);
        btn.setOnAction(event -> {
//            String text = textField.getText();
//            if (text != null && !text.isBlank()) {
//                int key = Integer.parseInt(text);
//                routerPane.setCurrentRoute(key);
//            }
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
