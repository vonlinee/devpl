package io.devpl.backend.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.devpl.backend.common.query.PageParam;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 业务层工具类
 */
public final class BusinessUtils {

    private BusinessUtils() {
    }

    /**
     * 分页查询
     *
     * @param param    分页参数
     * @param supplier 数据查询
     * @param <C>      集合类型
     * @param <T>      集合元素类型
     * @return 分页后的数据
     */
    public static <C extends Collection<T>, T> C startPage(PageParam param, Supplier<C> supplier) {
        try (Page<T> page = PageHelper.startPage(param.getPage(), param.getPageSize(), true, false, false)) {
            return supplier.get();
        }
    }

    /**
     * 分页查询
     *
     * @param param    分页参数
     * @param supplier 数据查询
     * @param <C>      集合类型
     * @param <T>      集合元素类型
     * @param <P>      分页参数子类型
     * @return 分页后的数据
     * @see PageParam
     */
    public static <C extends Collection<T>, T, P extends PageParam> C startPage(P param, Function<P, C> supplier) {
        try (Page<T> page = PageHelper.startPage(param.getPage(), param.getPageSize(), true, false, false)) {
            return supplier.apply(param);
        }
    }

    /**
     * 分页查询
     *
     * @param param    分页参数
     * @param supplier 数据查询逻辑
     * @param <C>      集合类型
     * @param <T>      集合元素类型
     * @param <P>      分页参数子类型
     * @return 分页信息
     * @see PageParam
     */
    public static <C extends Collection<T>, T, P extends PageParam> PageInfo<T> startPageInfo(P param, Function<P, C> supplier) {
        try (Page<T> page = PageHelper.startPage(param.getPage(), param.getPageSize(), true, false, false)) {
            supplier.apply(param);
            return new PageInfo<>(page);
        }
    }

    /**
     * 判断是否是true
     *
     * @param val 瓦尔
     * @return boolean
     */
    public static boolean isTrue(Object val) {
        if (val == null) {
            return false;
        }
        if (val instanceof Boolean b) {
            return b;
        }
        if (val instanceof Number n) {
            return n.intValue() > 0;
        }
        if (val instanceof String s) {
            return "true".equalsIgnoreCase(s);
        }
        return false;
    }

    /**
     * 判断是否是false
     *
     * @param val 瓦尔
     * @return boolean
     */
    public static boolean isFalse(Object val) {
        if (val == null) {
            return false;
        }
        if (val instanceof Boolean b) {
            return !b;
        }
        if (val instanceof Number n) {
            return n.intValue() < 0;
        }
        if (val instanceof String s) {
            return "false".equalsIgnoreCase(s);
        }
        return false;
    }
}
