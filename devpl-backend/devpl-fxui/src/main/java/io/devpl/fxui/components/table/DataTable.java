package io.devpl.fxui.components.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 仅适用于数据展示的表格，带分页，不需要自定义列
 *
 * @param <R> 行数据类型
 * @see javafx.scene.control.TableView
 * @see javafx.scene.control.skin.TableViewSkin
 * @see DataTableSkin
 */
public class DataTable<R> extends Control {

    /**
     * 一页数据大小
     */
    private final IntegerProperty pageSize = new SimpleIntegerProperty(10);

    public final int getPageSize() {
        return pageSize.get();
    }

    public final IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public final void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    /**
     * 所有的记录
     */
    private final ObservableList<R> items = FXCollections.observableArrayList();

    public final ObservableList<R> getItems() {
        return items;
    }

    private final ObjectProperty<Class<R>> rowClass = new SimpleObjectProperty<>();

    public final Class<R> getRowClass() {
        return rowClass.get();
    }

    public ObjectProperty<Class<R>> rowClassProperty() {
        return rowClass;
    }

    public final void setRowClass(Class<R> rowClass) {
        this.rowClass.set(rowClass);
    }

    private DataTableBehavior<R> behavior;

    public DataTable(Class<R> rowModel) {
        rowClass.set(rowModel);
    }

    public final void setBehavior(DataTableBehavior<R> behavior) {
        this.behavior = Objects.requireNonNull(behavior, "behavior cannot be null!");
        this.behavior.setDataTable(this);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DataTableSkin<>(this);
    }

    public final void addAll(List<R> records) {
        items.addAll(records);
    }

    @SafeVarargs
    public final void addAll(R... records) {
        items.addAll(records);
    }

    public final void add(R record) {
        items.add(record);
    }

    /**
     * 最大页数
     */
    private final IntegerProperty maxPageNum = new SimpleIntegerProperty(10);

    public final int getMaxPageNum() {
        return maxPageNum.get();
    }

    public final void setMaxPageNum(int maxPageNum) {
        this.maxPageNum.set(maxPageNum);
    }

    public final IntegerProperty maxPageNumProperty() {
        return maxPageNum;
    }

    public final DataTableBehavior<R> getBehavior() {
        return behavior == null ? new DefaultDataTableBehavior<>(this) : behavior;
    }

    /**
     * 新增一行
     */
    public final void appendRow() {
        R row = behavior.newItem(getRowClass());
        if (row != null) {
            getItems().add(row);
        }
    }

    public void setData(Collection<R> data) {
        if (!data.isEmpty()) {
            items.clear();
        }
        items.addAll(data);
    }
}
