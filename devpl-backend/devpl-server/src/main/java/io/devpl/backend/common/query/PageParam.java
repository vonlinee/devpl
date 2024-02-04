package io.devpl.backend.common.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

/**
 * 分页查询参数父类
 * jackson默认使用setter/getter映射JSON数据而不是字段名
 */
public class PageParam implements Serializable {

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

    public <T> IPage<T> asPage() {
        return new Page<>(page, limit);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
