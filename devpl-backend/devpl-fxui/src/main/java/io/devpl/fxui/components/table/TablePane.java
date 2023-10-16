package io.devpl.fxui.components.table;

import io.devpl.fxui.utils.FXUtils;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * 重写 DataTable 组件
 */
public class TablePane<T> extends BorderPane {

    TableView<T> tableView;
    PaginationControl pagination;
    TableOperation<T> operation;

    public TablePane(Class<T> rowModelClass) {

        this.tableView = FXUtils.createTableView(rowModelClass);
        tableView.setEditable(false);

        this.pagination = new PaginationControl();

        setCenter(tableView);
        setBottom(pagination);
    }

    public void initialize() {
        this.pagination.setOnCurrentPageChanged(event -> {
            if (operation != null) {
                TableData<T> tableData = operation.loadPage(event.getPageNum(), event.getPageSize());
                updateTableData(tableData);
                // 更新分页信息
                pagination.updatePageNums(event.getPageSize(), tableData.getTotalRows());
            }
        });

        pagination.setCurrentPageNum(1);
    }

    public void setTableOperation(TableOperation<T> operation) {
        this.operation = operation;
    }

    void updateTableData(TableData<T> tableData) {
        tableView.getItems().clear();
        tableView.getItems().addAll(tableData.getRows());
    }

}
