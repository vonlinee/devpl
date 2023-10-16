package io.devpl.fxui.components.table;

import java.io.Serializable;
import java.util.List;

/**
 * 表格数据
 *
 * @param <T> 表格数据类型
 */
public class TableData<T> implements Serializable {

    /**
     * 数据
     */
    private List<T> rows;

    /**
     * 是否还有数据
     */
    private boolean moreRows;

    /**
     * 总记录数
     */
    private long totalRows;

    public TableData(List<T> rows, boolean moreRows, long totalRows) {
        this.rows = rows;
        this.moreRows = moreRows;
        this.totalRows = totalRows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setMoreRows(boolean moreRows) {
        this.moreRows = moreRows;
    }

    public boolean hasMoreRows() {
        return moreRows;
    }

    public static <T> TableData<T> of(List<T> data) {
        return new TableData<>(data, true, data.size());
    }
}
