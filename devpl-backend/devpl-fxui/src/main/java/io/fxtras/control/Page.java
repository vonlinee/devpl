package io.fxtras.control;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

/**
 * <a href="https://www.cnblogs.com/zwbsoft/p/15923707.html">...</a>
 * @param <T>
 */
public class Page<T> {
    private SimpleIntegerProperty totalRecord; // total record number in source data
    private SimpleIntegerProperty pageSize; // the number of data in per page
    private SimpleIntegerProperty totalPage; // total page number
    private List<T> rowDataList; // total data

    /**
     * @param rowDataList
     * @param pageSize    the number of data in per page
     */
    public Page(List<T> rowDataList, int pageSize) {
        this.totalRecord = new SimpleIntegerProperty();
        this.totalPage = new SimpleIntegerProperty();
        this.rowDataList = rowDataList;
        this.pageSize = new SimpleIntegerProperty(pageSize);
        initialize();
    }

    private void initialize() {
        totalRecord.set(rowDataList.size());

        // calculate the number of total pages
        totalPage.set(totalRecord.get() % pageSize.get() == 0 ? totalRecord.get() / pageSize.get() : totalRecord.get() / pageSize.get() + 1);

        // add listener: the number of total pages need to be change if the page size changed
        pageSize.addListener((observable, oldVal, newVal) -> totalPage.set(totalRecord.get() % pageSize.get() == 0 ? totalRecord.get() / pageSize.get() : totalRecord.get() / pageSize.get() + 1));
    }

    /**
     * current page number(0-based system)
     * @param currentPage current page number
     * @return
     */
    public List<T> getCurrentPageDataList(int currentPage) {
        int fromIndex = pageSize.get() * currentPage;
        int tmp = pageSize.get() * currentPage + pageSize.get() - 1;
        int endIndex = tmp >= totalRecord.get() ? totalRecord.get() - 1 : tmp;

        // subList(fromIndex, toIndex) -> [fromIndex, toIndex)
        return rowDataList.subList(fromIndex, endIndex + 1);
    }

    public IntegerProperty totalPageProperty() {
        return totalPage;
    }
}

