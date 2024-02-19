package io.devpl.fxui.app;

import io.fxtras.control.SplitPane;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
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

        SplitPane root = new SplitPane();

        AnchorPane pane1 = new AnchorPane();
//        pane1.setStyle("-fx-background-color: red");
        AnchorPane pane2 = new AnchorPane();
//        pane2.setStyle("-fx-background-color: yellow");
        AnchorPane pane3 = new AnchorPane();
//        pane3.setStyle("-fx-background-color: green");


        MFXButton mfxButton = new MFXButton("Hello");
        mfxButton.setButtonType(ButtonType.RAISED);

        pane1.getChildren().addAll(mfxButton);

        UserAgentBuilder.builder()
            .themes(JavaFXThemes.MODENA) // Optional if you don't need JavaFX's default theme, still recommended though
            .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
            .setDeploy(true) // Whether to deploy each theme's assets on a temporary dir on the disk
            .setResolveAssets(true) // Whether to try resolving @import statements and resources urls
            .build() // Assembles all the added themes into a single CSSFragment (very powerful class check its documentation)
            .setGlobal(); // Finally, sets the produced stylesheet as the global User-Agent stylesheet

        root.getItems().addAll(pane1, pane2, pane3);

        stage.setTitle("工具");
        stage.setScene(new Scene(pane1, 800, 500));
        stage.show();
    }
}
