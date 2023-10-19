package io.devpl.tookit.fxui.view;

import io.devpl.fxui.utils.ClassUtils;

import java.util.List;

/**
 * 默认实现
 * @param <R>
 */
class DefaultDataTableBehavior<R> extends DataTableBehavior<R> {
    public DefaultDataTableBehavior(DataTable<R> dataTable) {
        super(dataTable);
    }

    @Override
    public TableData<R> loadPage(int pageNum, int pageSize) {
        return new TableData<>(List.of(), false, 0);
    }

    @Override
    public R save(R record) {
        return record;
    }

    @Override
    public List<R> saveBatch(List<R> records) {
        return records;
    }

    @Override
    public R newItem(Class<R> rowClass) {
        try {
            return ClassUtils.instantiate(rowClass);
        } catch (Exception exception) {
            return null;
        }
    }
}
