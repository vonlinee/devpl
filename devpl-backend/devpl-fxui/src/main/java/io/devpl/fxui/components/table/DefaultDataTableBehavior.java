package io.devpl.fxui.components.table;

import io.devpl.fxui.utils.Utils;

import java.util.List;

/**
 * 默认实现
 *
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
            return Utils.instantiate(rowClass);
        } catch (Exception exception) {
            return null;
        }
    }
}
