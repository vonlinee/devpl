package io.devpl.fxui.components.table;

import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * 用于TablePane的弹窗，新增或者修改
 * 注意：新增和修改用的是同一个表单
 *
 * @param <F> 表单数据类型，通常是一个JavaFX属性类，支持数据绑定
 * @param <R> 行数据类型，可以是POJO，也可以是JavaFX属性类
 */
class TablePaneDialog<R, F> extends Dialog<F> {

    F formObject;
    R row;

    public TablePaneDialog(FormRenderer formRegion, EventHandler<ActionEvent> saveCallback, EventHandler<ActionEvent> updateCallback) {
        // 可改变大小
        this.setResizable(true);
        // 添加按钮
        this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);

        formRegion.setPrefSize(600.0, 400.0);

        // 事件回调
        Button okBtn = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setOnAction(event -> {
            if ("新增".equals(this.getTitle())) {
                saveCallback.handle(event);
            } else if ("修改".equals(this.getTitle())) {
                updateCallback.handle(event);
            }
            event.consume();
        });

        this.getDialogPane().setContent(formRegion);
    }

    public final void show(R obj) {
        if (obj == null) {
            this.setTitle("新增");
        } else {
            this.setTitle("修改");
        }
        this.row = obj;
        super.show();
    }

    public final void setPrefSize(double w, double h) {
        setWidth(w);
        setHeight(h);
    }

    /**
     * @return 可能为null
     */
    public final R getRowObject() {
        return row;
    }
}
