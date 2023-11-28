package io.devpl.backend.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询参数父类
 * jackson默认使用setter/getter映射JSON数据
 */
@Getter
@Setter
public class PageParam {

    /**
     * 页码，默认0，第一页
     */
    private int page = 0;

    /**
     * 每页记录条数，默认10
     */
    private int limit = 10;

    public int getPageIndex() {
        return page;
    }

    public void setPageIndex(Integer pageIndex) {
        if (pageIndex != null) {
            this.page = pageIndex;
        }
    }

    public int getPageSize() {
        return limit;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null) {
            this.limit = pageSize;
        }
    }
}
