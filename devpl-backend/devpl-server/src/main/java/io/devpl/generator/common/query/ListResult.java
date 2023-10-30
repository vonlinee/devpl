package io.devpl.generator.common.query;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 */
public class ListResult<T> extends AbstractResult<List<T>> implements Serializable {

    /**
     * 列表的记录总条数，前端组件自动计算分页
     */
    private long total;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public ListResult(List<T> list, long total) {
        this.setData(list);
        this.total = total;
    }

    public static <T> ListResult<T> ok(IPage<T> page) {
        return new ListResult<>(page.getRecords(), page.getTotal());
    }

    public static <T> ListResult<T> ok(List<T> list, long total) {
        return new ListResult<>(list, total);
    }

    public static <T> ListResult<T> ok(List<T> list) {
        return new ListResult<>(list, list.size());
    }

    public static <T> ListResult<T> error(String message) {
        ListResult<T> result = new ListResult<>(Collections.emptyList(), -1);
        result.setMsg(message);
        return result;
    }

    public final long getTotal() {
        return total;
    }

    public final void setTotal(long total) {
        this.total = total;
    }
}
