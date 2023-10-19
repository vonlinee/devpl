package io.devpl.fxui.controller.template;

import io.devpl.sdk.io.FileUtils;
import io.devpl.fxui.editor.CodeMirrorEditor;
import io.devpl.fxui.mvvm.FxmlBinder;
import io.devpl.fxui.mvvm.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

@FxmlBinder(location = "layout/template/template_manage.fxml")
public class TemplateManageView extends FxmlView {

    @FXML
    public VBox vboxRight;

    TemplateInfoTableView tbvTemplateInfo = new TemplateInfoTableView();

    CodeMirrorEditor codeEditor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vboxRight.getChildren().add(tbvTemplateInfo);
    }

    public void parseTemplate(MouseEvent mouseEvent) {

    }

    public void m1() throws IOException {
        // 初始化模板引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        final VelocityTemplateAnalyzer analyzer = new VelocityTemplateAnalyzer();
        String text = "";

        Path path = Path.of("D:/Temp/1.vm");

        Files.deleteIfExists(path);

        Path templateFile = Files.write(path, text.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        // 获取模板文件
        Template t = ve.getTemplate(templateFile.toAbsolutePath().toString());

        analyzer.analyze(t);
    }

    FileChooser chooser = new FileChooser();

    @FXML
    public void openFileChooser(ActionEvent actionEvent) {
        chooser.setInitialDirectory(new File("D:/Temp"));
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("仅支持vm文件", "vm"));
        File file = chooser.showOpenDialog(getStage(actionEvent));
        if (file != null) {
            System.out.println(file);
            codeEditor.setContent(FileUtils.readToString(file), false);
        }
    }
}
