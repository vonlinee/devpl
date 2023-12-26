package io.devpl.fxui.controller.template;

import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.fxui.controller.fields.FieldTreeTable;
import io.devpl.fxui.controls.FXUtils;
import io.devpl.fxui.model.FieldNode;
import io.devpl.sdk.io.FileUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 模板渲染
 */
public class TemplateRenderView extends SplitPane {

    TextArea textArea;
    FieldTreeTable fieldTreeTable;

    FileChooser fileChooser;

    TemplateEngine templateEngine = new VelocityTemplateEngine();

    public TemplateRenderView() {
        this.textArea = new TextArea();
        textArea.setFont(new Font(15));
        this.fieldTreeTable = new FieldTreeTable();
        VBox vBox = new VBox();

        ButtonBar buttonBar = new ButtonBar();

        Button loadBtn = new Button("加载模板");
        Button renderBtn = new Button("渲染");

        renderBtn.setOnAction(event -> {
            List<FieldNode> fields = fieldTreeTable.getFields();

            System.out.println(fields);
        });

        loadBtn.setOnAction(event -> {
            if (fileChooser == null) {
                fileChooser = new FileChooser();
                fileChooser.setTitle("选择模板");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Velocity Template Files", "*.vm"), new FileChooser.ExtensionFilter("所有文件", "*"));
            }
            Stage stage = FXUtils.getStage(event);
            fileChooser.setInitialDirectory(new File("").getAbsoluteFile());
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    this.textArea.setText(FileUtils.readUTF8String(selectedFile));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonBar.getButtons().addAll(renderBtn, loadBtn);

        vBox.getChildren().addAll(buttonBar, textArea);

        textArea.prefHeightProperty().bind(vBox.heightProperty().subtract(buttonBar.heightProperty()));



        getItems().addAll(vBox, fieldTreeTable);
    }
}
