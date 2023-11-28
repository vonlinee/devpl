package io.devpl.backend.common.mvc;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 基础Mapper
 */
public interface EntityMapper<T> extends BaseMapper<T> {

    default List<T> selectList() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查分页的数据
     *
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @return 一页的数据
     */
    default List<T> selectPage(int pageIndex, int pageSize) {
        return selectPage(pageIndex, pageSize, Wrappers.emptyWrapper());
    }

    /**
     * 查分页的数据
     *
     * @param pageIndex    页码
     * @param pageSize     每页大小
     * @param queryWrapper 查询条件
     * @return 一页的数据
     */
    default List<T> selectPage(int pageIndex, int pageSize, Wrapper<T> queryWrapper) {
        Page<T> page = selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return page.getRecords();
    }
}
