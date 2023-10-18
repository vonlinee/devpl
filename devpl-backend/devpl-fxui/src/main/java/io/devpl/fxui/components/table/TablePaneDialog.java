package io.devpl.fxui.components.table;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * 用于TablePane的弹窗，新增或者修改
 * @param <F> 表单数据类型，通常是一个JavaFX属性类，支持数据绑定
 * @param <R> 行数据类型，可以是POJO，也可以是JavaFX属性类
 */
class TablePaneDialog<R, F> extends Dialog<F> {

    /**
     * 表单
     */
    private final F formObject;

    public TablePaneDialog(F formObject, Node formNode, EventHandler<ActionEvent> onOkBtnClicked) {
        this.formObject = formObject;
        // 可改变大小
        this.setResizable(true);
        // 添加按钮
        this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);

        // 事件回调
        Button okBtn = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setOnAction(onOkBtnClicked);

        this.getDialogPane().setContent(formNode);
    }

    public final void show(R obj) {
        if (obj == null) {
            this.setTitle("新增");
        } else {
            this.setTitle("修改");
        }
        super.show();
    }
}
