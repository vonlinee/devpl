package io.devpl.sdk.rest;

import java.io.Serial;
import java.io.Serializable;

/**
 * 封装分页信息
 */
public class PageInfo implements Serializable {

    public static final PageInfo UNKNOWN = new PageInfo();

    @Serial
    private static final long serialVersionUID = -9005418320425464234L;

    private int pageIndex;
    private int pageNum;
    private int pageSize;
    private int nextPage;
    private int firstRowNum;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getFirstRowNum() {
        return firstRowNum;
    }

    public void setFirstRowNum(int firstRowNum) {
        this.firstRowNum = firstRowNum;
    }
}
