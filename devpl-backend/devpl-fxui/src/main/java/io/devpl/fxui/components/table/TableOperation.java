package io.devpl.fxui.components.table;

import java.util.List;

public interface TableOperation<R> {

    /**
     * 加载指定页的数据，页码切换后会调用此方法
     *
     * @param pageNum  第几页
     * @param pageSize 每页记录条数
     * @return 分页数据
     */
    TableData<R> loadPage(int pageNum, int pageSize);

    /**
     * 保存单条记录
     *
     * @param record 单条记录
     */
    void save(R record);

    /**
     * 批量保存记录
     *
     * @param records 多条记录
     */
    void saveBatch(List<R> records);

    /**
     * 创建一个空行
     *
     * @param rowClass 行数据类, java bean
     * @return 空对象
     */
    default R newItem(Class<R> rowClass) {
        return null;
    }
}
