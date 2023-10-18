package io.devpl.fxui.app;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.components.Loading;
import io.devpl.fxui.components.pane.HalfPane;
import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.view.DataTypeModel;
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

        DataTypeModel model = new DataTypeModel();

        Form loginForm = Form.of(
            Group.of(
                Field.ofStringType(model.typeNameProperty())
                    .label("Username"),
                Field.ofStringType(model.typeKeyProperty())
                    .label("Password")
                    .required("This field can’t be empty")
            )
        ).title("Login");

        box.getChildren().add(new FormRenderer(loginForm));

        box.getChildren().add(FXControl.button("persist", event -> {
            System.out.println(model);
            loginForm.persist();
            System.out.println(model);
        }));

        stage.setTitle("工具");
        stage.setScene(scene);
        stage.show();
    }
}
