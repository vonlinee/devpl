package io.devpl.generator.common;

/**
 * 分页查询参数父类
 */
public class PageQuery {

    /**
     * 页码，默认0，第一页
     */
    private int pageIndex = 0;

    /**
     * 每页记录条数，默认10
     */
    private int pageSize = 10;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        if (pageIndex != null) {
            this.pageIndex = pageIndex;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
    }
}
