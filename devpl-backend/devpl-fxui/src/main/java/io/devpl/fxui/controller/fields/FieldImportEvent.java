package io.devpl.fxui.controller.fields;

import io.devpl.tookit.fxui.model.FieldSpec;
import javafx.scene.control.TableView;
import lombok.Data;

@Data
public class FieldImportEvent {

    TableView<FieldSpec> tableView;
    String text;
}
