package io.devpl.generator.common.query;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
public class PageResult<T> extends Result<List<T>> implements Serializable {

    // 总记录数
    private int total;

    /**
     * 分页
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageResult(List<T> list, long total) {
        this.setData(list);
        this.total = (int) total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
