package io.devpl.fxui.view;

import io.devpl.codegen.parser.sql.SqlUtils;
import io.devpl.fxui.components.Message;
import io.devpl.fxui.components.TextRegion;
import io.devpl.fxui.mvvm.FxmlBinder;
import io.devpl.fxui.mvvm.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.Map;
import java.util.Set;

@FxmlBinder(location = "fxml/test.fxml")
public class TestView extends FxmlView {

    @FXML
    public TextRegion leftTextArea;
    @FXML
    public TextRegion rightTextArea;

    @FXML
    public void onButtonClick(ActionEvent actionEvent) {
        String text = leftTextArea.getText();
        if (text == null || text.isBlank()) {
            return;
        }
        try {
            Map<String, Set<String>> selectColumns = SqlUtils.getSelectColumns(text);
            rightTextArea.setText(String.valueOf(selectColumns));
        } catch (Exception exception) {
            Message.show(exception.getMessage());
        }
    }
}
