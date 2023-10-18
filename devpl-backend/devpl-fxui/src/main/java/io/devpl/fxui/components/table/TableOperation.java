package io.devpl.fxui.components.table;

import java.util.Collections;
import java.util.List;

/**
 * TablePane操作
 * @param <R>
 */
public interface TableOperation<F, R> {

    /**
     * 加载指定页的数据，页码切换后会调用此方法
     * @param pageNum  第几页
     * @param pageSize 每页记录条数
     * @return 分页数据
     */
    default TableData<R> loadPage(int pageNum, int pageSize) {
        return TableData.of(Collections.emptyList());
    }

    /**
     * 将表单对象转换为表格行对象
     * @param formObject 表单对象
     * @return 表格行对象
     */
    default R convert(F formObject) {
        return null;
    }

    /**
     * 保存单条记录
     * @param record 单条记录
     */
    default void save(R record) {
    }

    default void update(R record) {

    }

    /**
     * 批量保存记录
     * @param records 多条记录
     */
    default void saveBatch(List<R> records) {
    }

    /**
     * 创建一个空行
     * @param rowClass 行数据类, java bean
     * @return 空对象
     */
    default R newItem(Class<R> rowClass) {
        return null;
    }
}
