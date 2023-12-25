package io.devpl.fxui.controller.fields;

import io.devpl.codegen.parser.java.MetaField;
import io.devpl.sdk.io.FileUtils;
import io.devpl.fxui.editor.CodeMirrorEditor;
import io.devpl.fxui.editor.LanguageMode;
import io.devpl.fxui.utils.FileChooserDialog;
import io.devpl.fxui.model.FieldSpec;
import io.devpl.fxui.mvvm.FxmlBinder;
import io.devpl.fxui.mvvm.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlBinder(location = "layout/fields/FieldsImportJavaView.fxml")
public class JavaImportView extends FxmlView {

    @FXML
    public BorderPane bopRoot;

    CodeMirrorEditor editor = CodeMirrorEditor.newInstance(LanguageMode.JAVA);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bopRoot.setCenter(editor.getView());
    }

    @FXML
    public void chooseFile(ActionEvent actionEvent) {
        FileChooserDialog.showFileOpenDialog(getStage(actionEvent)).ifPresent(file -> {
            editor.setContent(FileUtils.readToString(file), true);
        });
    }

    @Subscribe
    public void parse(FieldImportEvent event) {

    }

    public List<FieldSpec> convert(List<MetaField> fieldMetaDataList) {
        List<FieldSpec> list = new ArrayList<>(fieldMetaDataList.size());
        for (MetaField fieldMetaData : fieldMetaDataList) {
            FieldSpec fieldSpec = new FieldSpec();
            fieldSpec.setFieldName(fieldMetaData.getName());
            fieldSpec.setFieldDescription(fieldMetaData.getDescription());
            list.add(fieldSpec);
        }
        return list;
    }
}
