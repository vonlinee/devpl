package io.fxtras.control;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

public class TableWithPaginationAndSorting<T> {
    private final Page<T> page;
    private TableView<T> tableView;
    private Pagination tableViewWithPaginationPane;

    /**
     * getter
     **/
    public TableWithPaginationAndSorting(Page<T> page, TableView<T> tableView) {
        this.page = page;
        this.tableView = tableView;
        tableViewWithPaginationPane = new Pagination();
        tableViewWithPaginationPane.pageCountProperty().bindBidirectional(page.totalPageProperty());
        updatePagination();
    }

    private void updatePagination() {
        tableViewWithPaginationPane.setPageFactory(pageIndex -> {
            tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
            return tableView;
        });
    }

    public Node getTableViewWithPaginationPane() {
        return tableViewWithPaginationPane;
    }
}
