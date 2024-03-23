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
     * 当前第几页 页码，默认0，第一页
     * PageHelper分页从1开始
     */
    private int page = 1;

    /**
     * 每页记录条数，默认10
     */
    private int limit = 10;

    /**
     * 开始行
     */
    private long startRow;

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

    public void setStartRow(long startRow) {
        this.startRow = startRow;
    }

    /**
     * 计算分页开始的行
     * select * from 表名 limit(curPage-1)*pageSize,pageSize;
     *
     * @return 第一行的位置 * 第几页
     */
    public long getStartRow() {
        if (page - 1 < 0) {
            return 0;
        }
        return (long) limit * (page - 1);
    }

    public void calculateStartRow() {
        this.startRow = getStartRow();
    }
}
