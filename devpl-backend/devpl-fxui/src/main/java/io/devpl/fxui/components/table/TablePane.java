package io.devpl.fxui.components.table;

import io.devpl.fxui.components.Modal;
import io.devpl.fxui.utils.FXUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * 重写 DataTable 组件
 */
public class TablePane<T> extends BorderPane {

    TableView<T> tableView;
    PaginationControl pagination;
    TableOperation<T> operation;

    Modal insertModal;

    TablePaneOption option;

    public TablePane(Class<?> modelClass) {
        this(TablePaneOption.model(modelClass).enablePagination(true).enableToolbar(true));
    }

    @SuppressWarnings("unchecked")
    public TablePane(TablePaneOption option) {
        this.option = option;

        this.tableView = (TableView<T>) FXUtils.createTableView(option.getModelClass());
        this.tableView.setEditable(false);
        setCenter(this.tableView);

        if (option.isPaginationEnabled() && option.isToolBarEnabled()) {
            HBox bottom = new HBox();
            setBottom(bottom);

            if (option.isPaginationEnabled()) {
                this.pagination = new PaginationControl();
                bottom.getChildren().add(this.pagination);
            }

            if (option.isToolBarEnabled()) {
                this.insertModal = new Modal();
                ToolBar toolBar = new ToolBar();
                toolBar.setPrefWidth(400.0);
                Button btnAddRow = new Button("新增");
                Button btnRefresh = new Button("刷新");

                btnAddRow.setOnAction(event -> insertModal.show());

                btnRefresh.setOnAction(event -> {
                    Platform.runLater(() -> {
                        System.out.println("reload ");
                        TableData<T> tableData = operation.loadPage(pagination.getCurrentPageNum(), pagination.getCurrentPageSize());
                        System.out.println(tableData.getRows());
                        updateTableData(tableData);
                        // 更新分页信息
                        pagination.updatePageNums(pagination.getCurrentPageSize(), tableData.getTotalRows());
                    });
                });

                toolBar.getItems().addAll(btnAddRow, btnRefresh);
                bottom.getChildren().add(toolBar);
            }
        }
    }

    public void initialize() {
        if (this.pagination != null) {
            this.pagination.setOnCurrentPageChanged(event -> {
                if (this.operation != null) {
                    TableData<T> tableData = this.operation.loadPage(event.getPageNum(), event.getPageSize());
                    updateTableData(tableData);
                    // 更新分页信息
                    pagination.updatePageNums(event.getPageSize(), tableData.getTotalRows());
                }
            });
            pagination.setCurrentPageNum(1);
        }
    }

    public void setTableOperation(TableOperation<T> operation) {
        this.operation = operation;
        this.initialize();
    }

    public final void setAddForm(Node insertForm) {
        if (option.isToolBarEnabled() && insertForm != null && insertModal != null) {
            insertModal.setContent(insertForm);
        }
    }

    void updateTableData(TableData<T> tableData) {
        tableView.getItems().clear();
        tableView.getItems().addAll(tableData.getRows());
    }
}
