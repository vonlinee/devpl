package io.devpl.fxui.components.table;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.utils.FXUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablePane<T> extends BorderPane {

    TablePaneOption option;
    TableView<T> tableView;
    Pagination pagination;
    TableOperation<Object, T> operation;
    TablePaneDialog<T, ?> dialog;

    // 维护行选中的状态
    Map<Integer, BooleanProperty> rowSelectedStatus = new HashMap<>();

    @SuppressWarnings("unchecked")
    public TablePane(TablePaneOption option) {
        this.option = option;
        this.tableView = new TableView<>();
        // 双击行进行编辑
        this.tableView.setRowFactory(param -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (FXUtils.isPrimaryButtonDoubleClicked(event)) {
                    T item = row.getItem();
                    if (item != null) {
                        Object newFormObject = operation.convert(option.getFormObject(), item);
                        dialog.show(item);
                    }
                }
            });
            return row;
        });
        this.tableView = FXUtils.initTableViewColumns(this.tableView, (Class<T>) option.getModelClass());

        addCheckStatusColumnForTable(tableView);

        if (option.isPaginationEnabled()) {
            this.setBottom(this.pagination = new Pagination());
            if (this.operation != null) {
                this.pagination.setOnCurrentPageChanged(event -> {
                    TableData<T> tableData = this.operation.loadPage(event.getPageNum(), event.getPageSize());
                    updateTableData(tableData);
                    // 更新分页信息
                    pagination.updatePageNums(event.getPageSize(), tableData.getTotalRows());
                });
            }
        }
        addOperationContextMenu(this.tableView);
        setCenter(this.tableView);
    }

    public final List<Integer> getSelectedIndeies() {
        List<Integer> indexies = new ArrayList<>();
        for (Map.Entry<Integer, BooleanProperty> entry : rowSelectedStatus.entrySet()) {
            if (entry.getValue().get()) {
                indexies.add(entry.getKey());
            }
        }
        return indexies;
    }

    /**
     * 添加右键菜单
     *
     * @param tableView TableView
     */
    void addOperationContextMenu(TableView<T> tableView) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setPrefWidth(200);
        contextMenu.getItems().add(FXControl.menuItem("删除", event -> {
            T selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                operation.delete(selectedItem);
                tableView.getItems().remove(selectedItem);
            }
        }));

        contextMenu.getItems().add(FXControl.menuItem("新增", event -> this.dialog.show(null)));

        MenuItem refreshMenuItem = FXControl.menuItem("刷新", event -> Platform.runLater(() -> {
            TableData<T> tableData = operation.loadPage(pagination.getCurrentPageNum(), pagination.getCurrentPageSize());
            updateTableData(tableData);
            // 更新分页信息
            pagination.updatePageNums(pagination.getCurrentPageSize(), tableData.getTotalRows());
        }));

        contextMenu.getItems().add(refreshMenuItem);

        // 表单弹窗
        Form form = option.getFormCreator().apply(option.getFormObject());
        this.dialog = new TablePaneDialog<>(new FormRenderer(form), event -> {
            T record = operation.convert(option.getFormObject());
            operation.save(record);
            pagination.toLastPage();
            // 刷新数据
            Event.fireEvent(refreshMenuItem, new ActionEvent());
        }, event -> {
            T record = operation.convert(option.getFormObject());
            operation.update(record);
            // 刷新数据
            Event.fireEvent(refreshMenuItem, new ActionEvent());
        });

        tableView.setContextMenu(contextMenu);
    }

    /**
     * 添加选中状态列
     *
     * @param tableView 表格
     */
    void addCheckStatusColumnForTable(TableView<T> tableView) {
        TableColumn<T, Boolean> idColumn = new TableColumn<>("#");
        idColumn.setEditable(true);
        FXUtils.fixWidth(idColumn, 30.0);
        idColumn.setCellFactory(param -> {
            CheckBoxTableCell<T, Boolean> cell = new CheckBoxTableCell<>();
            cell.setSelectedStateCallback(param1 -> rowSelectedStatus.computeIfAbsent(param1, (p) -> new SimpleBooleanProperty()));
            cell.setEditable(true);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        tableView.getColumns().add(idColumn);
    }

    public void initialize() {
        if (this.pagination != null) {
            pagination.setCurrentPageNum(1);
        }
    }

    @SuppressWarnings("unchecked")
    public <F, R> void setTableOperation(TableOperation<F, R> operation) {
        this.operation = (TableOperation<Object, T>) operation;
        sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                initialize();
            }
        });
    }

    /**
     * 更新一页的数据，先清空在重新插入
     *
     * @param tableData 表格数据
     */
    void updateTableData(TableData<T> tableData) {
        tableView.getItems().clear();
        tableView.getItems().addAll(tableData.getRows());

        if (!rowSelectedStatus.isEmpty()) {
            for (Map.Entry<Integer, BooleanProperty> entry : rowSelectedStatus.entrySet()) {
                BooleanProperty value = entry.getValue();
                if (value != null) {
                    value.set(false);
                }
            }
        }
    }
}
