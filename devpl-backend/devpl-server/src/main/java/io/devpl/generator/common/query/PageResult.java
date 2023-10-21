package io.devpl.generator.common.query;

import io.devpl.generator.common.exception.StatusCode;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
@Data
public class PageResult<T> implements Serializable {

    // 编码 0表示成功，其他值表示失败
    private int code = 0;
    // 消息内容
    private String msg = "success";
    // 总记录数
    private int total;
    // 列表数据
    private List<T> list;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = (int) total;
    }

    public static <T> PageResult<T> ok(List<T> list) {
        PageResult<T> result = new PageResult<>(list, list.size());
        result.setCode(StatusCode.OK.getCode());
        result.setMsg(StatusCode.OK.getMsg());
        return result;
    }
}
