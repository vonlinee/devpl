package io.devpl.fxui.components.table;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;

/**
 * Default Skin of DataTable
 *
 * @param <R>
 * @see DataTable
 */
class DataTableSkin<R> extends SkinBase<DataTable<R>> {

    DataTableView<R> tableView;
    PaginationControl pagination;
    DataTableBehavior<R> behavior;

    private static final Insets NO_MARGIN = new Insets(0);

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected DataTableSkin(DataTable<R> control) {
        super(control);
        tableView = new DataTableView<>();
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // 不会再更改 items 列表本身，会更改元素
        tableView.setItems(control.getItems());

        pagination = new PaginationControl(control);
        behavior = control.getBehavior();
        control.addEventHandler(PagingEvent.CHANGE, event -> behavior.loadData(event.getPageNum(), event.getPageSize()));

        // 初始化表的列
        behavior.initTableView(tableView, control.getRowClass());

        // 触发首次加载
        pagination.updatePages(1);

        getChildren().addAll(tableView, pagination);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double paginationY = contentHeight - pagination.getHeight();
        layoutInArea(tableView, contentX, contentY, contentWidth, paginationY, 0, NO_MARGIN, HPos.CENTER, VPos.CENTER);
        layoutInArea(pagination, contentX, paginationY, contentWidth, pagination.getHeight(), 0, NO_MARGIN, HPos.CENTER, VPos.CENTER);
    }
}
