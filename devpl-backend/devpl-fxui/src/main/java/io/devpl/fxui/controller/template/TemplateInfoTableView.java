package io.devpl.fxui.controller.template;

import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.ResourceUtils;
import io.devpl.sdk.util.StringUtils;
import io.devpl.fxui.editor.CodeMirrorEditor;
import io.devpl.fxui.editor.LanguageMode;
import io.fxtras.utils.EventUtils;
import io.devpl.fxui.model.TemplateInfo;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

/**
 * 模板信息表
 */
public class TemplateInfoTableView extends TableView<TemplateInfo> {

    private final CodeMirrorEditor editor = CodeMirrorEditor.newInstance(LanguageMode.VELOCITY);

    private Dialog<ButtonType> dialog;

    public TemplateInfoTableView() {
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<TemplateInfo, String> templateNameColumn = new TableColumn<>("模板名称");
        templateNameColumn.setCellValueFactory(new PropertyValueFactory<>("templateName"));
        templateNameColumn.setMaxWidth(150.0);
        templateNameColumn.setMinWidth(150.0);
        TableColumn<TemplateInfo, String> templatePathColumn = new TableColumn<>("模板路径");
        templatePathColumn.setCellValueFactory(new PropertyValueFactory<>("templatePath"));
        TableColumn<TemplateInfo, Boolean> builtinColumn = new TableColumn<>("是否内置");
        builtinColumn.setCellValueFactory(new PropertyValueFactory<>("builtin"));
        builtinColumn.setMaxWidth(60.0);
        builtinColumn.setMinWidth(60.0);
        TableColumn<TemplateInfo, String> remarkColumn = new TableColumn<>("备注信息");
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remark"));

        getColumns().add(templateNameColumn);
        getColumns().add(templatePathColumn);
        getColumns().add(builtinColumn);
        getColumns().add(remarkColumn);

        Platform.runLater(() -> dialog = createDialog());

        setRowFactory(param -> {
            TableRow<TemplateInfo> row = new TableRow<>();
            row.setTextAlignment(TextAlignment.CENTER);
            row.setOnMouseClicked(event -> {
                if (!EventUtils.isPrimaryButtonDoubleClicked(event)) {
                    return;
                }
                TemplateInfo item = row.getItem();
                if (item == null) {
                    return;
                }
                File file = new File(item.getTemplatePath());
                if (file.exists()) {
                    String content = FileUtils.readToString(file);
                    if (StringUtils.hasText(content)) {
                        editor.setContent(content, true);
                        dialog.setTitle(item.getTemplatePath());
                        Optional<ButtonType> buttonTypeOptional = dialog.showAndWait();
                        if (buttonTypeOptional.isPresent()) {
                            ButtonType btnType = buttonTypeOptional.get();
                            if (btnType == ButtonType.OK) {
                                System.out.println(btnType);
                            }
                        }
                    }
                }
            });
            return row;
        });
    }

    public Dialog<ButtonType> createDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL, ButtonType.FINISH);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().setContent(editor.getView());
        dialog.setOnCloseRequest(event -> {
            editor.setContent("", true);
            // TODO 添加模板编辑保存功能
        });
        return dialog;
    }

    public void refreshItemsData() {
        super.refresh();

        URL resource = Thread.currentThread().getContextClassLoader().getResource("/");

        System.out.println(resource);

        File file = ResourceUtils.getResourcesAsFile("/mybatis-plus-generator/src/main/resources/templates");
        for (File fileItem : FileUtils.listFiles(file)) {
            TemplateInfo templateInfo = new TemplateInfo();
            templateInfo.setTemplateId(UUID.randomUUID().toString());
            templateInfo.setBuiltin(true);
            templateInfo.setTemplateName(fileItem.getName());
            templateInfo.setTemplatePath(fileItem.getAbsolutePath());
            getItems().add(templateInfo);
        }
    }
}
