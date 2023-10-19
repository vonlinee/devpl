package io.devpl.fxui.components.table;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.sun.javafx.event.EventUtil;
import io.devpl.fxui.utils.FXControl;
import io.devpl.fxui.utils.FXUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TablePane<T> extends BorderPane {

    TableView<T> tableView;
    PaginationControl pagination;
    TableOperation<Object, T> operation;
    TablePaneDialog<T, ?> dialog;
    TablePaneOption option;

    // 维护行选中的状态
    Map<Integer, SimpleBooleanProperty> rowSelectedStatus = new HashMap<>();

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

        this.tableView.getColumns().add(idColumn);
        setCenter(this.tableView);

        this.tableView = FXUtils.initTableViewColumns(this.tableView, (Class<T>) option.getModelClass());

        if (option.isPaginationEnabled() && option.isToolBarEnabled()) {
            HBox bottom = new HBox();
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(20);
            setBottom(bottom);

            if (option.isPaginationEnabled()) {
                this.pagination = new PaginationControl();
                this.pagination.setOnCurrentPageChanged(event -> {
                    if (this.operation != null) {
                        TableData<T> tableData = this.operation.loadPage(event.getPageNum(), event.getPageSize());
                        updateTableData(tableData);
                        // 更新分页信息
                        pagination.updatePageNums(event.getPageSize(), tableData.getTotalRows());
                    }
                });
                HBox.setHgrow(this.pagination, Priority.ALWAYS);
                bottom.getChildren().add(this.pagination);
            }

            if (option.isToolBarEnabled()) {

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

                this.tableView.setContextMenu(contextMenu);

                // 表单弹窗
                Form form = option.getFormCreator().apply(option.getFormObject());
                this.dialog = new TablePaneDialog<>(new FormRenderer(form), event -> {
                    T record = operation.convert(option.getFormObject());
                    operation.save(record);
                    pagination.toLastPage();
                    // 刷新数据
                    EventUtil.fireEvent(refreshMenuItem, new ActionEvent());
                }, event -> {
                    T record = operation.convert(option.getFormObject());
                    operation.update(record);
                    // 刷新数据
                    EventUtil.fireEvent(refreshMenuItem, new ActionEvent());
                });
            }
        }
    }

    public final List<Integer> getSelectedIndeies() {
        return rowSelectedStatus.entrySet().stream().filter(entry -> entry.getValue().get()).map(Map.Entry::getKey).collect(Collectors.toList());
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

        for (Map.Entry<Integer, SimpleBooleanProperty> entry : rowSelectedStatus.entrySet()) {
            SimpleBooleanProperty value = entry.getValue();
            if (value != null) {
                value.set(false);
            }
        }
    }
}
