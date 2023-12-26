package io.devpl.fxui.controller.fields;

import io.devpl.fxui.model.FieldNode;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * 导入SQL，支持DDL和查询SQL
 */
public class SqlParseView extends FieldParseView {

    TextArea textArea;

    @Override
    String getName() {
        return "SQL";
    }

    @Override
    Node createRootNode() {
        textArea = new TextArea();
        textArea.prefHeightProperty().bind(this.heightProperty());
        return textArea;
    }

    @Override
    public String getParseableText() {
        return textArea.getText();
    }

    @Override
    public void fillSampleText() {
        textArea.setText(getSampleText());
    }

    @Override
    public List<FieldNode> parse(String text) {
        return super.parse(text);
    }

    @Override
    public String getSampleText() {
        return """
            SELECT id, school_id, wisdom_exam_room, campus_monitor
            FROM external_system_param_config
            WHERE school_id = #{schoolId}
              """;
    }
}
