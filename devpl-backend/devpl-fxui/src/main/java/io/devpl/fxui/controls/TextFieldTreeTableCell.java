package io.devpl.fxui.controls;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableRow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

/**
 * @param <S>
 * @param <T>
 * @see javafx.scene.control.cell.TextFieldTreeTableCell
 */
public class TextFieldTreeTableCell<S, T> extends TreeTableCellBase<S, T> {

    private TextField textField;
    private final StringConverter<T> converter;
    private Node node;

    public TextFieldTreeTableCell(StringConverter<T> converter) {
        this.converter = converter;
        this.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, new CornerRadii(0), new BorderWidths(0)))); // 去掉单元格边框
        // 单击编辑
        this.setOnMouseClicked(event -> edit());
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();
        setDisclosureNodeVisiable(false);
        if (isEditing()) {
            if (textField == null) {
                textField = new TextField();
            }
            CellUtils.startEdit(this, this.converter, null, null, textField);
        }
    }

    private void setDisclosureNodeVisiable(boolean flag) {
        TreeTableRow<S> row = getTableRow();
        if (row != null) {
            DisclosureNode disclosureNode = (DisclosureNode) row.getDisclosureNode();
            disclosureNode.setShowing(flag);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, this.converter, null);
        System.out.println("取消编辑");
        setDisclosureNodeVisiable(true);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        CellUtils.updateItem(this, this.converter, null, null, textField);
    }
}
