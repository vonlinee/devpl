package io.devpl.fxui.components.table;

import io.devpl.fxui.utils.FXUtils;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Add '--add-exports javafx.controls/com.sun.javafx.scene.control.inputmap=fxui' to module compiler options
 *
 * @param <R>
 */
public abstract class DataTableBehavior<R> {

    protected DataTable<R> dataTable;
    private final TableColumninitializer<R> columnInitializer = new TableColumninitializer<>();

    public DataTableBehavior(DataTable<R> dataTable) {
        this.dataTable = dataTable;
    }

    public final void setDataTable(DataTable<R> dataTable) {
        this.dataTable = dataTable;
    }

    public final void loadData(int pageNum, int pageSize) {
        TableData<R> tableData = loadPage(pageNum, pageSize);
        long totalRows = tableData.getTotalRows();
        final int maxPageNum = calculateMaxPageNum(pageSize, totalRows);
        if (dataTable.getMaxPageNum() != maxPageNum) {
            dataTable.setMaxPageNum(maxPageNum);
        }
        dataTable.getItems().clear();
        dataTable.getItems().addAll(tableData.getRows());
    }

    /**
     * 加载指定页的数据，页码切换后会调用此方法
     *
     * @param pageNum  第几页
     * @param pageSize 每页记录条数
     * @return 分页数据
     */
    public abstract TableData<R> loadPage(int pageNum, int pageSize);

    /**
     * 保存单条记录
     *
     * @param record 单条记录
     */
    public abstract R save(R record);

    /**
     * 批量保存记录
     *
     * @param records 多条记录
     */
    public abstract List<R> saveBatch(List<R> records);

    /**
     * 计算最大页数
     *
     * @param pageSize      每页记录条数
     * @param totalRowCount 总记录数
     * @return 最大页数
     */
    public int calculateMaxPageNum(int pageSize, long totalRowCount) {
        long pages = totalRowCount / (long) pageSize;
        return pages == 0 ? 1 : (int) pages;
    }

    /**
     * 创建一个空行
     *
     * @param rowClass 行数据类, java bean
     * @return 空对象
     */
    public R newItem(Class<R> rowClass) {
        return null;
    }

    public void initTableView(TableView<R> tableView, Class<R> rowClass) {
        FXUtils.initTableView(tableView, rowClass);
        tableView.getColumns().addAll(getColumns(rowClass));
    }

    /**
     * 初始化表格
     *
     * @param rowClass 表格
     */
    public <C> List<TableColumn<R, C>> getColumns(Class<R> rowClass) {
        return columnInitializer.initColumns(rowClass);
    }
}
