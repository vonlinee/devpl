package io.devpl.fxui.components.table;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import io.devpl.fxui.utils.FXUtils;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * 重写 DataTable 组件
 */
public class TablePane<T> extends BorderPane {

    TableView<T> tableView;
    PaginationControl pagination;
    TableOperation<Object, T> operation;

    TablePaneDialog<?, ?> dialog;
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
            bottom.setAlignment(Pos.CENTER);
            bottom.setSpacing(20);
            setBottom(bottom);

            if (option.isPaginationEnabled()) {
                this.pagination = new PaginationControl();
                HBox.setHgrow(this.pagination, Priority.ALWAYS);
                bottom.getChildren().add(this.pagination);
            }

            if (option.isToolBarEnabled()) {
                // 表单
                Form form = option.getFormCreator().apply(option.getFormObject());
                this.dialog = new TablePaneDialog<>(option.getFormObject(), new FormRenderer(form), event -> {
                    form.persist();
                    T record = operation.convert(option.getFormObject());
                    operation.save(record);
                });

                ToolBar toolBar = new ToolBar();
                toolBar.setPrefWidth(400.0);
                Button btnAddRow = new Button("新增");
                Button btnRefresh = new Button("刷新");

                btnAddRow.setOnAction(event -> this.dialog.show(null));

                btnRefresh.setOnAction(event -> Platform.runLater(() -> {
                    TableData<T> tableData = operation.loadPage(pagination.getCurrentPageNum(), pagination.getCurrentPageSize());
                    updateTableData(tableData);
                    // 更新分页信息
                    pagination.updatePageNums(pagination.getCurrentPageSize(), tableData.getTotalRows());
                }));
                HBox.setHgrow(toolBar, Priority.ALWAYS);
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

    @SuppressWarnings("unchecked")
    public <F, R> void setTableOperation(TableOperation<F, R> operation) {
        this.operation = (TableOperation<Object, T>) operation;
        this.initialize();
    }

    void updateTableData(TableData<T> tableData) {
        tableView.getItems().clear();
        tableView.getItems().addAll(tableData.getRows());
    }
}
