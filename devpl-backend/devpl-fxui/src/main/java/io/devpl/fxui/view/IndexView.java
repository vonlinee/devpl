package io.devpl.fxui.view;

import io.devpl.fxui.mvvm.FxmlBinder;
import io.devpl.fxui.mvvm.FxmlView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@FxmlBinder(location = "fxml/index.fxml")
public class IndexView extends FxmlView {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}