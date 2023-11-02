package io.devpl.generator.common.mvc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.generator.domain.param.Query;
import org.springframework.util.StringUtils;

/**
 * 基础服务类，所有Service都要继承
 */
public abstract class BaseServiceImpl<M extends EntityMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * 获取分页对象
     * @param query 分页参数
     */
    protected IPage<T> getPage(Query query) {
        Page<T> page = new Page<>(query.getPage(), query.getLimit());
        page.addOrder(OrderItem.desc("id"));
        return page;
    }

    protected QueryWrapper<T> getWrapper(Query query) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getCode()), "code", query.getCode());
        wrapper.like(StringUtils.hasText(query.getTableName()), "table_name", query.getTableName());
        wrapper.like(StringUtils.hasText(query.getAttrType()), "attr_type", query.getAttrType());
        wrapper.like(StringUtils.hasText(query.getColumnType()), "column_type", query.getColumnType());
        wrapper.like(StringUtils.hasText(query.getConnName()), "conn_name", query.getConnName());
        wrapper.eq(StringUtils.hasText(query.getDbType()), "db_type", query.getDbType());
        wrapper.like(StringUtils.hasText(query.getProjectName()), "project_name", query.getProjectName());
        return wrapper;
    }
}
