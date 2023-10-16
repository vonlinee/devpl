package io.devpl.fxui.utils;

import io.devpl.fxui.components.FXTableViewModel;
import io.devpl.fxui.components.table.TableColumninitializer;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;

import java.util.Comparator;
import java.util.List;

public class FXUtils {

    public static void layoutInArea(Region parent, Node child) {
        Region.layoutInArea(child,
            0, 0, parent.getWidth(), parent.getHeight(), 0,
            Insets.EMPTY, true, true, HPos.CENTER, VPos.CENTER, parent.isSnapToPixel());
    }

    public static <S> void setData(TableView<S> tableView, List<S> data) {
        ObservableList<S> items = tableView.getItems();
        if (items.size() > 0) {
            items.clear();
        }
        items.addAll(data);
    }

    /**
     * 创建 TableView 实例
     *
     * @param rowClass 行数据类型
     * @param <R>      行数据类型
     * @return TableView
     * @see io.devpl.fxui.components.FXTableViewColumn
     * @see FXTableViewModel
     */
    public static <R> TableView<R> createTableView(Class<R> rowClass) {
        TableView<R> tableView = new TableView<>();
        initTableView(tableView, rowClass);
        return tableView;
    }

    public static <R> void initTableView(TableView<R> tableView, Class<R> rowClass) {
        FXTableViewModel tableViewModel = rowClass.getAnnotation(FXTableViewModel.class);
        if (tableViewModel == null) {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } else {
            tableView.setColumnResizePolicy(tableViewModel.resizePolicy() == 1 ? TableView.CONSTRAINED_RESIZE_POLICY : TableView.UNCONSTRAINED_RESIZE_POLICY);
        }

        TableColumninitializer<R> initializer = new TableColumninitializer<>();

        // boolean order = tableViewModel != null && tableViewModel.enableOrder();
        List<TableColumn<R, Object>> tableColumns = initializer.initColumns(rowClass, false);
        tableView.getColumns().addAll(tableColumns);
    }
}
