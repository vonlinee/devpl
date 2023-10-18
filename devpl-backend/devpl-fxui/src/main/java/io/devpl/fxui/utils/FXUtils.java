package io.devpl.fxui.utils;

import io.devpl.fxui.components.table.FXTableViewModel;
import io.devpl.fxui.components.table.FXTableViewColumn;
import io.devpl.fxui.components.table.TableColumninitializer;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;

public class FXUtils {

    public static void layoutInRegion(Region parent, Node child) {
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
     * @param rowClass 行数据类型
     * @param <R>      行数据类型
     * @return TableView
     * @see FXTableViewColumn
     * @see FXTableViewModel
     */
    public static <R> TableView<R> createTableView(Class<R> rowClass) {
        TableView<R> tableView = new TableView<>();
        tableView.getColumns().addAll(createTableViewColumns(tableView, rowClass, new TableColumninitializer<>()));
        return tableView;
    }

    public static <R> List<TableColumn<R, Object>> createTableViewColumns(TableView<R> tableView, Class<R> rowClass, TableColumninitializer<R> columninitializer) {
        FXTableViewModel tableViewModel = rowClass.getAnnotation(FXTableViewModel.class);
        if (tableViewModel == null) {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } else {
            tableView.setColumnResizePolicy(tableViewModel.resizePolicy() == 1 ? TableView.CONSTRAINED_RESIZE_POLICY : TableView.UNCONSTRAINED_RESIZE_POLICY);
        }
        boolean order = tableViewModel != null && tableViewModel.orderFields();
        return columninitializer.initColumns(rowClass, order);
    }

    public static String color(Color color) {
        return color.toString();
    }
}
